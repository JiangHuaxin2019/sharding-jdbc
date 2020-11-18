package com.jhxapi.shardingjbbc.config.sharding;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.common.Result;
import com.sankuai.inf.leaf.segment.SegmentIDGenImpl;
import com.sankuai.inf.leaf.segment.dao.IDAllocDao;
import com.sankuai.inf.leaf.segment.dao.impl.IDAllocDaoImpl;

/**
 * 美团leaf id生成器
 * @author Administrator
 *
 */
@Component
public class LeafKeyGenerator {
	
	private Logger logger = LoggerFactory.getLogger(LeafKeyGenerator.class);

	/**
	 * leaf Id生成器
	 */
    private IDGen idGen;
    
    @Autowired
    public LeafKeyGenerator(@Qualifier("leaf") DataSource dataSource) {
    	IDAllocDao dao = new IDAllocDaoImpl(dataSource);
    	// Config ID Gen
        idGen = new SegmentIDGenImpl();((SegmentIDGenImpl) idGen).setDao(dao);
        if (idGen.init()) {
            logger.info("Segment Service Init Successfully");
        } else {
            throw new IllegalStateException("Segment Service Init Fail");
        }
    }
    
    public Result getId(String key) {
        return idGen.get(key);
    }

    public SegmentIDGenImpl getIdGen() {
        if (idGen instanceof SegmentIDGenImpl) {
            return (SegmentIDGenImpl) idGen;
        }
        return null;
    }

}
