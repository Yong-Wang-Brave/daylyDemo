package com.example.demo.mybatiesIntecet;
 
import java.util.List;
 
import org.apache.ibatis.annotations.Select;
 
import com.example.demo.mybatiesIntecet.Test;
 
 
public interface TestMapper {
	public List<Test> test();
}