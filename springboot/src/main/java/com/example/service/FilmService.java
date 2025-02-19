package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
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
        film.setTypeIds(JSONUtil.toJsonStr(film.getIds()));
        filmMapper.insert(film);
    }

    public void updateById(Film film) {
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
        }
        return films;
    }

    public List<Film> selectScoreTop10() {
        List<Film> films = filmMapper.selectScoreTop10();
        for (Film film : films) {
            initActors(film);
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
}
