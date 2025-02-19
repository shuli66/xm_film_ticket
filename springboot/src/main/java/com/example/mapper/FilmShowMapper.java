package com.example.mapper;

import com.example.entity.FilmShow;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FilmShowMapper {

    int insert(FilmShow filmShow);

    void updateById(FilmShow filmShow);

    void deleteById(Integer id);

    @Select("select * from `film_show` where id = #{id}")
    FilmShow selectById(Integer id);

    List<FilmShow> selectAll(FilmShow filmShow);

}
