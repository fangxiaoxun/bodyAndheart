package com.example.team_project.service.impl;

import com.example.team_project.dao.PostLikeDao;
import com.example.team_project.dao.impl.PostLikeDaoImpl;
import com.example.team_project.service.PostLikeService;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 动态点赞的业务逻辑层实现类
 */
public class PostLikeServiceImpl implements PostLikeService {
    private final PostLikeDao POST_LIKE_DAO = new PostLikeDaoImpl();

    @Override
    public boolean alreadyLike(long uid, long postId) {
        if (uid > 0 && postId > 0) {
            return POST_LIKE_DAO.alreadyLike(postId, uid);
        }else {
            return false;
        }
    }

    @Override
    public boolean[] alreadyLikes(long uid, long... postId) {
        if (postId != null && postId.length > 0) {
            //排序，从大到小
            TreeSet<Long> set = new TreeSet<>((o1, o2) -> (int) (o2 - o1));
            for (long id : postId) {
                set.add(id);
            }
            long[] sortedIds = new long[set.size()];
            int index = 0;
            for (Long id : set) {
                sortedIds[index++] = id;
            }
            return POST_LIKE_DAO.alreadyLikes(uid, sortedIds);
        }else {
            return null;
        }
    }

    @Override
    public boolean delete(long uid, long postId) {
        if (uid > 0 && postId > 0) {
            return POST_LIKE_DAO.delete(postId, uid);
        }else {
            return false;
        }
    }

    @Override
    public boolean like(long uid, long postId) {
        if (uid > 0 && postId > 0) {
            return POST_LIKE_DAO.like(uid, postId);
        }else {
            return false;
        }
    }

    @Override
    public long likeCount(long pid) {
        if (pid > 0) {
            return POST_LIKE_DAO.likeCount(pid);
        }else {
            return 0;
        }
    }
}
