package org.bedoing.blog.facade;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bedoing.blog.commons.TagsDict;
import org.bedoing.blog.constant.Constant;
import org.bedoing.blog.entity.Tag;
import org.bedoing.blog.service.IArticleService;
import org.bedoing.blog.vo.ResponseVO;
import org.bedoing.blog.vo.TagsVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Administrator on 2016/9/19.
 */
@RestController
@RequestMapping("/tag")
public class TagController extends BaseController {
    private static final Logger log = Logger.getLogger(TagController.class);

    @Resource
    private IArticleService articleService;

    @RequestMapping(value = "/group/{tagType}", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> tagsGroup(@PathVariable int tagType){
        List<TagsVO> result = articleService.tagsGroup(tagType);
        log.info(JSON.toJSONString(result));

        List<String> tagNameList = new ArrayList<String>();
        List<Integer> articleNumList = new ArrayList<Integer>();
        for (TagsVO t : result) {
            tagNameList.add(t.getTagName());
            articleNumList.add(t.getArticleNum());
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tagNames", tagNameList);
        map.put("counts", articleNumList);
        map.put("data", result);

        return map;
    }

    @RequestMapping(value = "/list", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TagsVO> allTags(){
        List<Tag> list = articleService.findTagsByType(-1);
        List<TagsVO> result = new ArrayList<TagsVO>();
        for (Tag t : list) {
            TagsVO vo = new TagsVO();
            vo.setTagId(t.getTagId());
            vo.setTagName(t.getTagName());
            vo.setTagType(t.getTagType());

            result.add(vo);
        }

        return result;
    }

    @RequestMapping(value = "/tags/{type}", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TagsVO> getTags(@PathVariable int type){
        List<Tag> list = articleService.findTagsByType(type);
        List<TagsVO> result = new ArrayList<TagsVO>();
        for (Tag t : list) {
            TagsVO vo = new TagsVO();
            vo.setTagId(t.getTagId());
            vo.setTagName(t.getTagName());
            vo.setTagType(t.getTagType());

            result.add(vo);
        }

        return result;
    }

    @RequestMapping(value = "/add", method = POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO addNewTag(@PathVariable String tagName, @PathVariable int tagType) {
        ResponseVO vo = new ResponseVO();
        String msg = "";
        // TODO
        if(tagType == -1) {
            msg = "请选择标签类型";
        }
        if(StringUtils.isBlank(tagName)) {
            msg = "空标签";
        }

        if(msg == "") {
            tagName = tagName.trim();
            if(TagsDict.getTagIdByName(tagName) != -1) {
                msg = "已存在标签";
            }else {
                Tag t = new Tag();
                t.setTagName(tagName);
                t.setTagType(tagType);
                int tagId = articleService.addTag(t);
                // TODO save error
                if(tagId != -1) {
                    TagsDict.tagsDict.put(tagId, tagName);

                    msg = Constant.SUCCESS;
                }
            }
        }
        vo.setRetMsg(msg);

        return vo;
    }
}
