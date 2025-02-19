package com.example.mapper;

import com.example.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentMapper {

    // 根据电影id查询电影的评论
    List<Comment> selectCommentsByFilmId(Integer filmId);

    // 查询所有评论
    List<Comment> selectAll();

    // 添加评论
    int insertComment(Comment comment);

    // 用户单个删除评论
    int deleteCommentByIdAndUserId(@Param("commentId") Integer commentId, @Param("userId") Integer userId);

    // 管理员删除评论
    int deleteCommentByAdmin(Integer commentId);

    // 更新评论
    int updateComment(Comment comment);

    // 批量删除评论
    int deleteBatch(Integer[] commentIds);

    // 根据评论id查询
    Optional<Comment> selectById(Integer commentId);

    // 根据用户id查询
    List<Comment> selectByUserId(Integer userId);

    // 根据电影id或评论内容查询
    List<Comment> selectByFilters(Long filmId, String commentText);
}

