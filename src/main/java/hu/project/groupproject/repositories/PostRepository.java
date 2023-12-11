package hu.project.groupproject.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import hu.project.groupproject.entities.MyPost;



// @Repository
public interface PostRepository extends JpaRepository<MyPost, Long>{
    
    // List<MyPost> findByLastName(String lastName);

    // List<MyPost> findByFirstNameLike(String firstName);
}
