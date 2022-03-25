package com.spider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spider.pojo.Car;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/11 23:05
 */
public interface CarMapper extends BaseMapper<Car> {

    /*获取车辆的种类：大型车、中型车、小型车*/
    @Select("SELECT car_grade, COUNT(car_grade) FROM car_detail GROUP BY car_grade ORDER BY COUNT(car_grade) DESC")
    List<String> getCarTypes();

    /*获取车辆的动力类型：前置四驱等(获取前6个数量最多的)*/
    @Select("SELECT power_type, COUNT(power_type) FROM car_detail GROUP BY power_type ORDER BY COUNT(power_type) DESC LIMIT 6")
    List<String> getPowerTypes();

    /*获取车辆的变速箱类型：自动、手动等*/
    @Select("SELECT transmission, COUNT(transmission) FROM car_detail GROUP BY transmission ORDER BY COUNT(transmission) DESC")
    List<String> getTransTypes();
}
