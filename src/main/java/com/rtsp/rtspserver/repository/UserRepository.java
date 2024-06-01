package com.rtsp.rtspserver.repository;

import com.rtsp.rtspserver.model.User;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private Connection conn;

    public UserRepository() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public User addUser(User user) {
        String sql = "INSERT INTO Users (user_login, role_id, hash_password) VALUES (?, ?, ?) RETURNING user_id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            System.out.println(user.getUserLogin() + user.getRoleId() + user.getHashPassword());
            pstmt.setString(1, user.getUserLogin());
            pstmt.setInt(2, user.getRoleId());
            pstmt.setString(3, user.getHashPassword());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user.setUserId(rs.getInt(1));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findUserById(int userId) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("user_id"), rs.getString("user_login"), rs.getInt("role_id"), rs.getString("hash_password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE Users SET user_login = ?, role_id = ?, hash_password = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserLogin());
            pstmt.setInt(2, user.getRoleId());
            pstmt.setString(3, user.getHashPassword());
            pstmt.setInt(4, user.getUserId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(rs.getInt("user_id"), rs.getString("user_login"), rs.getInt("role_id"), rs.getString("hash_password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    public Optional<User> findByUserLogin(String userLogin) {
        String sql = "SELECT * FROM Users WHERE user_login = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userLogin);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new User(
                        rs.getInt("user_id"),
                        rs.getString("user_login"),
                        rs.getInt("role_id"),
                        rs.getString("hash_password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
