package org.bedoing.blog.repository;

import org.bedoing.blog.entity.Subject;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by Ken on 2016/9/17.
 */
@Transactional
public interface SubjectRepository extends CrudRepository<Subject, Integer> {

}
