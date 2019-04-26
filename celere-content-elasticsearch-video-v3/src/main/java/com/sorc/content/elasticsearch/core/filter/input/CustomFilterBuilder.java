package com.sorc.content.elasticsearch.core.filter.input;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomFilterBuilder {

	Logger logger = LoggerFactory.getLogger(CustomFilterBuilder.class);

	public BoolQueryBuilder createFilter(BoolQueryBuilder filters) {

		if (filters != null) {
			BoolQueryBuilder filterBuilder = QueryBuilders.boolQuery();
			filterBuilder.must(filters);
			if (filterBuilder != null) {
				logger.debug(filterBuilder != null ? filterBuilder.toString()
						: "Filter is null");
			}
			return filterBuilder;
		} else {
			return null;
		}
	}
	
	public QueryBuilder createFilter(IElasticSearchFilter filter) {

		if (filter instanceof TermFilter) {
			return QueryBuilders.termQuery(((TermFilter) filter).getField(),
					((TermFilter) filter).getTerm());
		} else if (filter instanceof TermsFilter) {
			return QueryBuilders.termsQuery(((TermsFilter) filter).getField(),
					((TermsFilter) filter).getTerms());
		} else if (filter instanceof RangeFilter) {
			return QueryBuilders
					.rangeQuery(((RangeFilter) filter).getField())
					.to(((RangeFilter) filter).getTo())
					.from(((RangeFilter) filter).getFrom());
		} else if (filter instanceof ScriptFilter) {					
			return QueryBuilders.scriptQuery(((ScriptFilter) filter)
					.getScript());
		} else if(filter instanceof ExistsFilter) {
			return QueryBuilders.existsQuery(((ExistsFilter) filter).getField());
		} else if (filter instanceof DateRangeFilter) {
			return QueryBuilders
					.rangeQuery(((DateRangeFilter) filter).getField())
					.gt(((DateRangeFilter) filter).getFrom())
					.lt(((DateRangeFilter) filter).getTo());
		} else if(filter instanceof NumericRangeFilter) {
			return QueryBuilders.rangeQuery(((NumericRangeFilter) filter).getField()).gt(((NumericRangeFilter) filter).getFrom());
		}
		else {
			return null;
		}
	}
}
