package com.sorc.content.elasticsearch.core.filter.input;

import org.elasticsearch.index.query.BoolQueryBuilder;


public interface IElasticSearchQueryBuilder {

	BoolQueryBuilder buildQuery();
}
