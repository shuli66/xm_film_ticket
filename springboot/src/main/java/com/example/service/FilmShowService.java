package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.mapper.*;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 放映记录业务层方法
 */
@Service
public class FilmShowService {

    @Resource
    private FilmShowMapper filmShowMapper;
    @Resource
    private CinemaMapper cinemaMapper;
    @Resource
    private FilmMapper filmMapper;
    @Resource
    private RoomMapper roomMapper;
    @Resource
    private TypeMapper typeMapper;
    @Resource
    private ActorMapper actorMapper;

    public void add(FilmShow filmShow) {
        Account currentUser = TokenUtils.getCurrentUser();
        filmShow.setCinemaId(currentUser.getId());
        filmShowMapper.insert(filmShow);
    }

    public void updateById(FilmShow filmShow) {
        filmShowMapper.updateById(filmShow);
    }

    public void deleteById(Integer id) {
        filmShowMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            filmShowMapper.deleteById(id);
        }
    }

    public FilmShow selectById(Integer id) {
        FilmShow filmShow = filmShowMapper.selectById(id);
        Film film = filmMapper.selectById(filmShow.getFilmId());
        if (ObjectUtil.isNotEmpty(film)) {
            List<Integer> typeIds = JSONUtil.toList(film.getTypeIds(), Integer.class);
            List<String> types = new ArrayList<>();
            for (Integer typeId : typeIds) {
                Type type = typeMapper.selectById(typeId);
                if (ObjectUtil.isNotEmpty(type)) {
                    types.add(type.getTitle());
                }
            }
            film.setTypes(types);
            filmShow.setFilm(film);
        }
        Cinema cinema = cinemaMapper.selectById(filmShow.getCinemaId());
        if (ObjectUtil.isNotEmpty(cinema)) {
            filmShow.setCinemaName(cinema.getName());
        }
        Room room = roomMapper.selectById(filmShow.getRoomId());
        if (ObjectUtil.isNotEmpty(room)) {
            filmShow.setRoomName(room.getName());
        }
        return filmShow;
    }

    public List<FilmShow> selectAll(FilmShow filmShow) {
        return filmShowMapper.selectAll(filmShow);
    }

    public PageInfo<FilmShow> selectPage(FilmShow filmShow, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.CINEMA.name().equals(currentUser.getRole())) {
            filmShow.setCinemaId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<FilmShow> list = filmShowMapper.selectAll(filmShow);
        return PageInfo.of(list);
    }

    public List<Cinema> selectByFilmId(Integer filmId) {
        FilmShow filmShow = new FilmShow();
        filmShow.setFilmId(filmId);
        List<FilmShow> filmShows = filmShowMapper.selectAll(filmShow);
        List<FilmShow> collect = filmShows.stream().filter(x -> "购票中".equals(x.getStatus())).collect(Collectors.toList());
        List<Cinema> list = new ArrayList<>();
        for (FilmShow show : collect) {
            Cinema cinema = cinemaMapper.selectById(show.getCinemaId());
            if (ObjectUtil.isNotEmpty(cinema)) {
                list.add(cinema);
            }
        }
        return list;
    }

    public List<Film> selectByCinemaId(Integer cinemaId) {
        FilmShow filmShow = new FilmShow();
        filmShow.setCinemaId(cinemaId);
        List<FilmShow> filmShows = filmShowMapper.selectAll(filmShow);
        List<FilmShow> collect = filmShows.stream().filter(x -> "购票中".equals(x.getStatus())).collect(Collectors.toList());
        List<Film> list = new ArrayList<>();
        for (FilmShow show : collect) {
            Film film = filmMapper.selectById(show.getFilmId());
            if (ObjectUtil.isNotEmpty(film)) {
                List<String> tmpList = new ArrayList<>();
                List<Integer> ids = JSONUtil.toList(film.getTypeIds(), Integer.class);
                // 初始化电影分类信息
                for (Integer typeId : ids) {
                    Type type = typeMapper.selectById(typeId);
                    if (ObjectUtil.isNotEmpty(type)) {
                        tmpList.add(type.getTitle());
                    }
                }
                film.setTypes(tmpList);

                // 初始化演职人员信息
                Actor actor = new Actor();
                actor.setFilmId(film.getId());
                List<Actor> actorList = actorMapper.selectAll(actor);
                List<String> actors = actorList.stream().map(Actor::getName).collect(Collectors.toList());
                film.setActors(actors);

                list.add(film);
            }
        }
        return list;
    }
}
