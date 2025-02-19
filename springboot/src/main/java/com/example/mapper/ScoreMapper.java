package com.example.mapper;

import com.example.entity.Score;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ScoreMapper {

    int insert(Score score);

    void deleteById(Integer id);

    @Select("select * from `score` where id = #{id}")
    Score selectById(Integer id);

    List<Score> selectAll(Score score);

    @Select("select * from score where film_id = #{filmId}")
    List<Score> selectByFilmId(Integer filmId);

    // 获取所有评分
    @Select("select * from score")
    List<Score> getAllScores();
}
