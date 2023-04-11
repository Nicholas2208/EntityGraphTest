package com.nwhite;

import com.nwhite.model.Post;
import com.nwhite.repo.PostRepository;
import com.nwhite.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
public class App 
{
    public static void main( String[] args ) {
        Long postId = 1L;
        Post post = null;
        PostRepository postRepository = new PostRepository();

        //post = postRepository.find(postId);
        //post = postRepository.findWithEntityGraph(postId);
        //post = postRepository.findWithEntityGraph2(postId);
        //post = postRepository.findUsingJpql(postId);
        post = postRepository.findUsingCriteria(postId);
        System.out.println(post);
    }
}
