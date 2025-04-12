package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.core.date.DateUtil;
import com.example.entity.Actor;
import com.example.entity.Area;
import com.example.entity.Film;
import com.example.entity.Type;
import com.example.mapper.ActorMapper;
import com.example.mapper.AreaMapper;
import com.example.mapper.FilmMapper;
import com.example.mapper.TypeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

/**
 * 电影信息业务层方法
 */
@Service
public class FilmService {

    @Resource
    private FilmMapper filmMapper;
    @Resource
    private TypeMapper typeMapper;
    @Resource
    private AreaMapper areaMapper;
    @Resource
    private ActorMapper actorMapper;

    public void add(Film film) {
        // 自动设置电影状态
        setFilmStatus(film);
        
        film.setTypeIds(JSONUtil.toJsonStr(film.getIds()));
        filmMapper.insert(film);
    }

    public void updateById(Film film) {
        // 自动设置电影状态
        setFilmStatus(film);
        
        film.setTypeIds(JSONUtil.toJsonStr(film.getIds()));
        filmMapper.updateById(film);
    }

    public void deleteById(Integer id) {
        filmMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            filmMapper.deleteById(id);
        }
    }

    /**
     * 自动更新电影状态
     * 根据上映日期自动更新电影状态
     * 注意：不会改变已经设置为"停止上映"的电影状态
     */
    public void autoUpdateFilmStatus() {
        List<Film> films = filmMapper.selectAll(new Film());
        String currentDate = DateUtil.today();
        
        for (Film film : films) {
            // 如果电影已经是"停止上映"状态，则不做改变
            if ("停止上映".equals(film.getStatus())) {
                continue;
            }
            
            String startDate = film.getStart();
            if (startDate != null) {
                // 如果当前日期大于等于上映日期，且状态为"待上映"，则更新为"已上映"
                if (currentDate.compareTo(startDate) >= 0 && "待上映".equals(film.getStatus())) {
                    film.setStatus("已上映");
                    filmMapper.updateById(film);
                }
            }
        }
    }

    public Film selectById(Integer id) {
        Film film = filmMapper.selectById(id);
        List<Integer> ids = JSONUtil.toList(film.getTypeIds(), Integer.class);
        List<String> tmpList = new ArrayList<>();
        for (Integer typeId : ids) {
            Type type = typeMapper.selectById(typeId);
            if (ObjectUtil.isNotEmpty(type)) {
                tmpList.add(type.getTitle());
            }
        }
        Area area = areaMapper.selectById(film.getAreaId());
        if (ObjectUtil.isNotEmpty(area)) {
            film.setAreaName(area.getTitle());
        }
        film.setTypes(tmpList);
        return film;
    }

    public List<Film> selectAll(Film film) {
        return filmMapper.selectAll(film);
    }

    public PageInfo<Film> selectPage(Film film, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Film> list = filmMapper.selectAll(film);
        for (Film dbFilm : list) {
            List<Integer> ids = JSONUtil.toList(dbFilm.getTypeIds(), Integer.class);
            dbFilm.setIds(ids);
            List<String> tmpList = new ArrayList<>();
            for (Integer typeId : ids) {
                Type type = typeMapper.selectById(typeId);
                if (ObjectUtil.isNotEmpty(type)) {
                    tmpList.add(type.getTitle());
                }
            }
            dbFilm.setTypes(tmpList);
        }
        return PageInfo.of(list);
    }

    public List<Film> selectTotalTop10() {
        List<Film> films = filmMapper.selectTotalTop10();
        for (Film film : films) {
            initActors(film);
            initTypes(film);
        }
        return films;
    }

    public List<Film> selectScoreTop10() {
        List<Film> films = filmMapper.selectScoreTop10();
        for (Film film : films) {
            initActors(film);
            initTypes(film);
        }
        return films;
    }

    private void initActors(Film film) {
        Actor actor = new Actor();
        actor.setFilmId(film.getId());
        List<Actor> actors = actorMapper.selectAll(actor);
        List<String> actorsNameList = actors.stream().map(Actor::getName).collect(Collectors.toList());
        film.setActors(actorsNameList);
    }
    
    private void initTypes(Film film) {
        if (film.getTypeIds() == null || film.getTypeIds().isEmpty()) {
            return;
        }
        
        try {
            List<Integer> typeIds = JSONUtil.toList(film.getTypeIds(), Integer.class);
            List<String> typeNames = new ArrayList<>();
            
            for (Integer typeId : typeIds) {
                Type type = typeMapper.selectById(typeId);
                if (type != null) {
                    typeNames.add(type.getTitle());
                }
            }
            
            film.setTypes(typeNames);
        } catch (Exception e) {
            film.setTypes(new ArrayList<>());
        }
    }

    public Integer selectPriceRanking(Integer filmId) {
        List<Film> films = filmMapper.selectAll(new Film());
        List<Film> rankList = films.stream()
                .sorted(Comparator.comparingDouble(Film::getTotal).reversed())
                .collect(Collectors.toList());
        for (int i = 0; i < rankList.size(); i++) {
            if (rankList.get(i).getId().equals(filmId)) {
                return i + 1;
            }
        }
        return null;
    }




    public List<Film> getFilmsByUserId(Integer id) {
        return filmMapper.getFilmsByUserId(id);
    }

    public List<Film> getFilmsByCategory(Integer key) {
        return filmMapper.getFilmsByCategory(key);
    }

    /**
     * 根据电影上映日期自动设置状态
     * 如果已经是"停止上映"状态，则保持不变
     */
    private void setFilmStatus(Film film) {
        // 如果是修改操作且状态已经是"停止上映"，则保持状态不变
        if (film.getId() != null) {
            Film existingFilm = filmMapper.selectById(film.getId());
            if (existingFilm != null && "停止上映".equals(existingFilm.getStatus())) {
                film.setStatus("停止上映");
                return;
            }
        }
        
        // 如果管理员手动设置为"停止上映"，则保持状态不变
        if ("停止上映".equals(film.getStatus())) {
            return;
        }
        
        // 根据上映日期自动设置状态
        String startDate = film.getStart();
        if (startDate != null) {
            String currentDate = DateUtil.today();
            if (currentDate.compareTo(startDate) >= 0) {
                film.setStatus("已上映");
            } else {
                film.setStatus("待上映");
            }
        }
    }
}
