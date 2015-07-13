/**
 *
 */
package com.ycsoft.business.dto.core.prod;

import java.util.List;

import com.ycsoft.beans.prod.PPromotionTheme;
import com.ycsoft.beans.prod.PPromotionThemeCounty;

/**
 * @author YC-SOFT
 *
 */
public class PromotionThemeDto {
	PPromotionTheme theme;
	private List<PromotionDto> proms;
	private List<PPromotionThemeCounty> countys;

	public List<PromotionDto> getProms() {
		return proms;
	}

	public List<PPromotionThemeCounty> getCountys() {
		return countys;
	}

	public void setCountys(List<PPromotionThemeCounty> countys) {
		this.countys = countys;
	}

	public void setProms(List<PromotionDto> proms) {
		this.proms = proms;
	}

	public PPromotionTheme getTheme() {
		return theme;
	}

	public void setTheme(PPromotionTheme theme) {
		this.theme = theme;
	}
}
