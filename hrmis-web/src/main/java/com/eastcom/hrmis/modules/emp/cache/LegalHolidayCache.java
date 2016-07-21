package com.eastcom.hrmis.modules.emp.cache;

import com.eastcom.baseframe.common.context.SpringContextHolder;
import com.eastcom.baseframe.common.utils.CacheUtils;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.hrmis.modules.emp.entity.LegalHoliday;
import com.eastcom.hrmis.modules.emp.service.LegalHolidayService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 法定节假日Cache
 * @author wutingguang <br>
 */
public class LegalHolidayCache {

	public static final String CACHE_NAME = "LEGAL_HOLIDAY_CACHE";
	public static final String CACHE_MAP = "legalHolidayMap";
	public static final String CACHE_LIST = "legalHolidayList";
	
	private static final LegalHolidayService legalHolidayService = SpringContextHolder.getBean(LegalHolidayService.class);

	/**
	 * 得到list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<LegalHoliday> getList(){
		List<LegalHoliday> result = (List<LegalHoliday>) CacheUtils.get(CACHE_NAME,CACHE_LIST);
		if (result == null){
			result = legalHolidayService.find(new HashMap<String, Object>());
			if (CollectionUtils.isNotEmpty(result)) {
				CacheUtils.put(CACHE_NAME,CACHE_LIST,result);
			}
		}
		return result;
	}
	
	/**
	 * 得到Map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, LegalHoliday> getMap(){
		Map<String, LegalHoliday> result = (Map<String, LegalHoliday>) CacheUtils.get(CACHE_NAME,CACHE_MAP);
		if (result == null) {
			List<LegalHoliday> holidays = getList();
			if (CollectionUtils.isNotEmpty(holidays)) {
				result = Maps.newHashMap();
				for (LegalHoliday legalHoliday : holidays) {
					result.put(DateUtils.formatDate(legalHoliday.getHoliday()), legalHoliday);
				}
			}
		}
		return result;
	}
	
	/**
	 * 判断是否法定节假日
	 * @param date
	 * @return
	 */
	public static boolean isLegalHoliday(Date date){
		return isLegalHoliday(DateUtils.formatDate(date));
	}
	
	/**
	 * 判断是否法定节假日
	 * @param date
	 * @return
	 */
	public static boolean isLegalHoliday(String date){
		Map<String, LegalHoliday> map = getMap();
		if (map.get(date) != null) {
			return true;
		}
		return false;
	}

	/**
	 * 得到统计月份中，节假日期清单
	 * @param date
	 * @return
	 * @throws Exception
     */
	@SuppressWarnings("deprecation")
	public static List<String> getLegalHolidaysByDate(Date date) throws Exception{
		List<String> result = Lists.newArrayList();
		//日期头
		int maxDay = date.getDate();
		for (int i = 1; i <= maxDay; i++) {
			date.setDate(i);
			if(LegalHolidayCache.isLegalHoliday(date)){
				result.add(DateUtils.formatDate(date));
			}
		}
		return result;
	}

	/**
	 * 得到统计月份中，节假日期清单
	 * @param statMonth yyyy年-MM月
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<String> getLegalHolidaysByMonth(String statMonth) throws Exception{
		List<String> result = Lists.newArrayList();
		//日期头
		Date date = DateUtils.parseDate(statMonth, "yyyy年-MM月");
		int maxDay = DateUtils.getMonthMaxDay(date);
		for (int i = 1; i <= maxDay; i++) {
			date.setDate(i);
			if(LegalHolidayCache.isLegalHoliday(date)){
				result.add(DateUtils.formatDate(date));
			}
		}
		return result;
	}
	
	/**
	 * 根据员工入职时间，判断须排除在外的节假日天数
	 * 
	 * @param startDate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<String> getCurrentMonthExcludeLegalHolidaysByEntryDate(Date enrtyDate){
		List<String> result = Lists.newArrayList();
		//日期头
		int maxDay = enrtyDate.getDate();
		for (int i = 1; i <= maxDay; i++) {
			enrtyDate.setDate(i);
			if(LegalHolidayCache.isLegalHoliday(enrtyDate)){
				result.add(DateUtils.formatDate(enrtyDate));
			}
		}
		return result;
	}
	
	/**
	 * 根据员工离职时间，判断须排除在外的节假日天数
	 * @param quitCompanyDate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<String> getCurrentMonthExcludeLegalHolidaysByQuitCompanyDate(Date quitCompanyDate){
		List<String> result = Lists.newArrayList();
		//日期头
		int startDay = quitCompanyDate.getDate();
		int maxDay = DateUtils.getMonthMaxDay(quitCompanyDate);
		for (int i = startDay; i <= maxDay; i++) {
			quitCompanyDate.setDate(i);
			if(LegalHolidayCache.isLegalHoliday(quitCompanyDate)){
				result.add(DateUtils.formatDate(quitCompanyDate));
			}
		}
		return result;
	}
	
	/**
	 * 清除当前权限缓存
	 */
	public static void clearCache(){
		CacheUtils.remove(CACHE_NAME, CACHE_MAP);
		CacheUtils.remove(CACHE_NAME, CACHE_LIST);
	}
	
}
