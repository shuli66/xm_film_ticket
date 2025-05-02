package com.example.service;


import com.example.entity.Comment;
import com.example.mapper.CommentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;


    // 查询所有评论
    public PageInfo<Comment> selectAllComments(Integer pageNum, Integer pageSize) {
        // 使用 PageHelper 启动分页
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(commentMapper.selectAll());
    }

    // 根据电影id或者评论内容查询电影的评论
    public PageInfo<Comment> selectCommentsByFilters(Long filmId, String commentText, Integer pageNum, Integer pageSize) {
        // 创建分页查询对象
        PageHelper.startPage(pageNum, pageSize);

        // 进行条件查询
        List<Comment> comments = commentMapper.selectByFilters(filmId, commentText);

        // 使用 PageInfo 封装分页结果
        return new PageInfo<>(comments);
    }



    public boolean addComment(Comment comment) {
        return commentMapper.insertComment(comment) > 0;
    }


    //用户只能删除自己的评论
    public boolean deleteComment(Integer commentId, Integer userId) {
        // 确保用户只能删除自己的评论
        return commentMapper.deleteCommentByIdAndUserId(commentId, userId) > 0;
    }



    public boolean updateComment(Comment comment) {
        return commentMapper.updateComment(comment) > 0;
    }


    public boolean deleteBatchComment(Integer[] commentIds) {
        return commentMapper.deleteBatch(commentIds) > 0;
    }

    public boolean deleteCommentByAdmin(Integer commentId) {
        // 查找评论
        Comment comment = commentMapper.selectById(commentId).orElse(null);
        if (comment == null) {
            return false; // 评论不存在
        }

        // 执行删除
        commentMapper.deleteCommentByAdmin(commentId);
        return true;
    }

    /**
     * 通过评论ID获取评论信息
     *
     * @param commentId 评论ID
     * @return 返回评论对象，若未找到则返回null
     */
    public Comment getCommentById(Integer commentId) {
        return commentMapper.selectById(commentId).orElse(null);
    }

    /**
     * 通过用户id获取评论信息
     */
    public List<Comment> selectByUserId(Integer userId) {
        return commentMapper.selectByUserId(userId);
    }

    /**
     * 获取指定电影的评论数量
     * @param filmId 电影ID
     * @return 评论数量
     */
    public long countByFilmId(Integer filmId) {
        List<Comment> comments = commentMapper.selectByFilters(filmId != null ? filmId.longValue() : null, null);
        return comments != null ? comments.size() : 0;
    }

}
