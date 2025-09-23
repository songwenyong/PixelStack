package com.pixelstack.ims.mapper;

import com.github.pagehelper.Page;
import com.pixelstack.ims.domain.Image;
import com.pixelstack.ims.domain.ImageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageMapper {

    int addImage(Image image);

    int deleteImage(@Param("iid") int iid);

    int addTiltle(@Param("title") String title, @Param("iid") int iid);

    Image getImageByiid(@Param("iid") int iid);

    int getImageStarCount(@Param("iid") int iid);

    int getImageThumbCount(@Param("iid") int iid);

    List<String> getImageAllTagsByiid(@Param("iid") int iid);

    Page<ImageInfo> getImageList();

    List<ImageInfo> getImageListByUid(int uid);

    Integer getUidbyImage(int iid);

    List<ImageInfo> selectMyStars(@Param("list") List<Integer> list);

    List<ImageInfo> getListByTagName(String tagname);

    int updateCount(int iid);

    int updateTitle(@Param("iid") int iid, @Param("title") String title);

    List<ImageInfo> getListByTitleOrAuthor(@Param("type") String type, @Param("search") String search);

}
