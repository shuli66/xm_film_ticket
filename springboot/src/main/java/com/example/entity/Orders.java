package com.example.entity;

import java.util.List;

public class Orders {
    private Integer id; // 主键id
    private String orderNo;// 订单号
    private Integer userId;// 用户id
    private Integer filmId;// 电影id
    private Integer cinemaId;// 影院id
    private Integer roomId;// 影厅id
    private String time;//  放映time
    private String createTime;// 创建时间
    private String seat;  // [1排2座,3排4座]
    private Double price;// 价格
    private String status;// 订单状态

    private List<Seat> seatList;
    private String userName;// 用户名称
    private String filmName;// 电影名称
    private String filmImg;// 电影图片
    private String cinemaName;// 影院名称
    private String roomName;// 影厅名称
    private Integer showId;// 场次id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFilmId() {
        return filmId;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    public Integer getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Integer cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getFilmImg() {
        return filmImg;
    }

    public void setFilmImg(String filmImg) {
        this.filmImg = filmImg;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public Integer getShowId() {
        return showId;
    }

    public void setShowId(Integer showId) {
        this.showId = showId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", userId=" + userId +
                ", filmId=" + filmId +
                ", cinemaId=" + cinemaId +
                ", roomId=" + roomId +
                ", time='" + time + '\'' +
                ", createTime='" + createTime + '\'' +
                ", seat='" + seat + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", seatList=" + seatList +
                ", userName='" + userName + '\'' +
                ", filmName='" + filmName + '\'' +
                ", filmImg='" + filmImg + '\'' +
                ", cinemaName='" + cinemaName + '\'' +
                ", roomName='" + roomName + '\'' +
                ", showId=" + showId +
                '}';
    }
}
