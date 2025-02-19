package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import com.example.entity.Account;
import com.example.entity.Film;
import com.example.entity.Score;
import com.example.exception.CustomException;
import com.example.mapper.FilmMapper;
import com.example.mapper.ScoreMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 电影评分业务层方法
 */
@Service
public class ScoreService {

    @Resource
    private ScoreMapper scoreMapper;
    @Resource
    private FilmMapper filmMapper;

    @Transactional
    public void add(Score score) {
        Account currentUser = TokenUtils.getCurrentUser();
        score.setUserId(currentUser.getId());
        // 要先判断一下该用户对该电影有没有进行过评分
        List<Score> list = scoreMapper.selectAll(score);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new CustomException("500", "您已经对该电影评过分了，请勿重复评分");
        }
        scoreMapper.insert(score);
        // 计算一下该电影的评分
        // 把该电影的所有评分都查出来，然后计算一下平均分
        List<Score> listFilm = scoreMapper.selectByFilmId(score.getFilmId());
        double sum = listFilm.stream().mapToDouble(Score::getScore).sum();

        Double average = sum / listFilm.size();

        // 更新一下电影的评分信息
        Film film = filmMapper.selectById(score.getFilmId());
        film.setScore(average);
        filmMapper.updateById(film);
    }

    public void deleteById(Integer id) {
        scoreMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            scoreMapper.deleteById(id);
        }
    }

    public Score selectById(Integer id) {
        return scoreMapper.selectById(id);
    }

    public List<Score> selectAll(Score score) {
        return scoreMapper.selectAll(score);
    }

    public PageInfo<Score> selectPage(Score score, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Score> list = scoreMapper.selectAll(score);
        return PageInfo.of(list);
    }

}
