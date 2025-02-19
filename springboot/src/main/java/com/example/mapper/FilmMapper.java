package com.example.mapper;

import com.example.entity.Film;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FilmMapper {

    int insert(Film film);

    void updateById(Film film);

    void deleteById(Integer id);

    @Select("select * from `film` where id = #{id}")
    Film selectById(Integer id);

    List<Film> selectAll(Film film);

    @Select("select * from film order by total desc limit 10")
    List<Film> selectTotalTop10();
    @Select("select * from film order by score desc limit 10")
    List<Film> selectScoreTop10();

    List<Film> getFilmsByUserId(Integer id);

    List<Film> getFilmsByCategory(Integer key);
}
