package com.example.controller;

import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Comment;
import com.example.entity.User;
import com.example.service.CommentService;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/comment")
public class CommentController {


    @Resource
    private CommentService commentService; // 评论服务类

    @Resource
    private UserService userService; // 用户服务类


    /**
     * 新增评论
     */
    @PostMapping("/addComment")
    public Result addComment(@RequestBody Comment comment) {
        commentService.addComment(comment);
        return Result.success("评论提交成功");
    }



//    /**
//     * 查询所有评论
//     */
//    @GetMapping("/selectAllComments")
//    public Result selectAllComments(@RequestParam(defaultValue = "1") Integer pageNum,
//                                    @RequestParam(defaultValue = "10") Integer pageSize) {
//        // 获取评论列表
//        PageInfo<Comment> comments = commentService.selectAllComments(pageNum, pageSize);
//
//        // 遍历评论并获取每个评论者的 userName
//        for (Comment comment : comments.getList()) {
//            Integer userId = comment.getUserId();
//            User user = userService.getUserById(userId);  // 假设有方法获取用户信息
//            comment.setUserName(user.getName());     // 将 userName 设置到评论对象中
//            comment.setUserAvatar(user.getAvatar());     // 如果有头像，也可以设置
//        }
//
//        return Result.success(comments);
//    }


    /**
     * 根据评论id查询评论
     *
     */
    @GetMapping("/selectCommentById/{commentId}")
    public Result selectComment(@PathVariable Integer commentId) {
        // 获取评论对象
        Comment comment = commentService.getCommentById(commentId);

        if (comment == null) {
            return Result.error(ResultCodeEnum.DATA_ERROR.code, "未找到该评论");
        }

        Integer userId = comment.getUserId();
        User user = userService.getUserById(userId);  // 假设有方法获取用户信息
        comment.setUserName(user.getName());     // 将 userName 设置到评论对象中
        comment.setUserAvatar(user.getAvatar());     // 如果有头像，也可以设置

        return Result.success(comment);
    }


    /**
     * 根据电影id和评论内容查询电影评论
     */
    @GetMapping("/selectComments")
    public Result selectComments(@RequestParam(required = false) Long filmId,
                                 @RequestParam(required = false) String commentText,
                                 @RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize) {

        // 打印参数确认是否正确接收
        System.out.println("filmId: " + filmId);
        System.out.println("commentText: " + commentText);

        // 获取评论列表，根据传入的条件进行筛选
        PageInfo<Comment> comments = commentService.selectCommentsByFilters(filmId, commentText, pageNum, pageSize);

        // 遍历评论并获取每个评论者的 userName
        for (Comment comment : comments.getList()) {
            Integer userId = comment.getUserId();
            User user = userService.getUserById(userId);  // 假设有方法获取用户信息
            comment.setUserName(user.getName());     // 将 userName 设置到评论对象中
            comment.setUserAvatar(user.getAvatar());     // 如果有头像，也可以设置
        }

        return Result.success(comments);
    }




    /**
     * 用户删除评论
     */
    @DeleteMapping("/deleteComment/{commentId}")
    public Result deleteComment(@PathVariable Integer commentId, @RequestParam Integer userId) {
        boolean success = commentService.deleteComment(commentId, userId);

        if (success) {
            return Result.success("评论删除成功");
        } else {
            return Result.error(ResultCodeEnum.FAIL.code, "评论删除失败或没有权限");
        }
    }
    /**
     * 管理员删除评论
     */
    @DeleteMapping("/deleteCommentByAdmin/{commentId}")
    public Result deleteCommentByAdmin(@PathVariable Integer commentId) {
        // 直接调用删除方法，不做用户权限验证
        boolean success = commentService.deleteCommentByAdmin(commentId);

        if (success) {
            return Result.success("评论删除成功");
        } else {
            return Result.error(ResultCodeEnum.FAIL.code, "评论删除失败");
        }
    }





    @PutMapping("/updateComment")
    public Result updateComment(@RequestBody Comment comment) {
        // 校验评论是否存在及用户权限
        if (comment.getId() == null || comment.getUserId() == null || comment.getCommentText() == null) {
            return Result.error(ResultCodeEnum.PARAM_ERROR.code, "评论内容不能为空");
        }

        // 设置修改时间
        // 设置当前时间为修改时间
        comment.setCreateTime(LocalDateTime.now());

        boolean success = commentService.updateComment(comment);
        if (success) {
            return Result.success("评论更新成功");
        } else {
            return Result.error(ResultCodeEnum.FAIL.code, "评论更新失败或没有权限");
        }
    }


    /**
     * 批量删除评论
     */
    @DeleteMapping("/deleteBatchComment")
    public Result deleteBatchComment(@RequestBody Integer[] commentIds) {
        boolean success = commentService.deleteBatchComment(commentIds);
        if (success) {
            return Result.success("批量删除成功");
        } else {
            return Result.error(ResultCodeEnum.FAIL.code, "批量删除失败");
        }
    }

}
