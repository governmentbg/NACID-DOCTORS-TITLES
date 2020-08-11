package com.nacid.web.model.inquires;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.nacid.bl.applications.TrainingInstitutionDataProvider;
import com.nacid.bl.inquires.ApplicationTypeAndEntryNumSeries;
import com.nacid.bl.inquires.CompanyApplicantRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.UniversityDataProvider;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import org.springframework.util.CollectionUtils;

public class InquiresUtils {
	public static List<Integer> generateElementList(HttpServletRequest request, String idsAttributeName, String idAttributeName) {
		//Tozi method dobavq i elementa, koito e v combobox-a, no ne e dobaven v gorniq spisyk!!!
		TreeSet<Integer> result = RequestParametersUtils.convertRequestParameterToIntegerTreeSet(request.getParameter(idsAttributeName));
		Integer attrId = idAttributeName == null ? null : DataConverter.parseInteger(request.getParameter(idAttributeName), null);
		if (attrId != null) {
			if (result == null) {
				result = new TreeSet<Integer>();
			}
			result.add(attrId);
		} 
		return result == null ? null : new ArrayList<Integer>(result);
	}
	/**
	 * dobavq elementite, koito sa maski (primerno specialnost Авиа*)
	 * @param request
	 * @param idsAttributeName
	 * @param currentIds
	 * @param nomenclatureId
	 * @return
	 */
	public static List<Integer> addMaskedElements(HttpServletRequest request, String idsAttributeName, String idAttributeName, List<Integer> currentIds, int nomenclatureId) {
		String names = request.getParameter(idsAttributeName);
		String param = request.getParameter(idAttributeName);
		Set<Integer> result = new HashSet<Integer>();
		if (currentIds != null) {
            result.addAll(currentIds);
        }
		if (!StringUtils.isEmpty(names) || !StringUtils.isEmpty(param)) {
			List<String> namesLst = new ArrayList<String>();
			if (!StringUtils.isEmpty(names)) {
				String[] namesArr = names.split(";");
				if (namesArr != null && namesArr.length > 0) {
					namesLst.addAll(Arrays.asList(namesArr));
				}	
			}
			if (!StringUtils.isEmpty(param)) {
				/*if (param.charAt(param.length() - 1) != '*') {
					param = param + "*";
				}
                param = param.replace("*", "%");*/
				namesLst.add(param);
			}
			if (namesLst.size() > 0) {
				NacidDataProvider nacidDataProvider = NacidBaseRequestHandler.getNacidDataProvider(request.getSession());
				NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
				for (String str:namesLst) {
					if ("".equals(str)) {
						continue;
					}
                    if (str.charAt(str.length() -1 ) != '*') {
                        str += "*";
                    }
                    str = str.replace("*", "%");
					List<? extends FlatNomenclature> nomenclatures = null;
					nomenclatures = nomenclaturesDataProvider.getFlatNomenclaturesContainingNameLike(nomenclatureId, str, null, null);
					if (nomenclatures != null) {
						for (FlatNomenclature nom:nomenclatures) {
							result.add(nom.getId());
						}
					}
				}
			}
		}
		return result.size() == 0 ? null : new ArrayList<Integer>(result);
	}
	/**
	 * generira universityCountryIds i universityIds ot request-a.
	 * @param request
	 * @param universityIds
	 * @param countryIds
	 * @param nacidDataProvider
	 * 
	 * @return dali ima nqkakvi danni za universities - t.e. opredelq dali trqbva da se nulira universityIds ako size()-a mu e 0
	 */
	public static boolean generateUniversityCountryAndUniversityIds(HttpServletRequest request, List<Integer> universityIds, List<Integer> countryIds, NacidDataProvider nacidDataProvider) {
		int universitiesCount = DataConverter.parseInt(request.getParameter("universities_count"), 0);
		UniversityDataProvider universityDataProvider = nacidDataProvider.getUniversityDataProvider();
		boolean containsData = false;
		Set<Integer> _universityIds = new HashSet<>();
		Set<Integer> _countryIds = new HashSet<>();
		for (int i = 0; i < universitiesCount; i++) {
			List<Integer> uniIds = generateElementList(request, "university" + i + "Ids", "university" + i + "Id");
			String uniNamesRequestParam = DataConverter.parseString(request.getParameter("university" + i + "NamesIds"),"");
			String uniNameRequestParam = request.getParameter("university" + i);

			String orgUniNamesRequestParam = DataConverter.parseString(request.getParameter("org_university" + i + "NamesIds"),"");
			String orgUniNameRequestParam = request.getParameter("org_university" + i);

			Integer countryId = DataConverter.parseInteger(request.getParameter("_university" + i + "Country"), null);
			boolean noUniNames = StringUtils.isEmpty(uniNameRequestParam) && StringUtils.isEmpty(uniNamesRequestParam) && StringUtils.isEmpty(orgUniNameRequestParam) && StringUtils.isEmpty(orgUniNamesRequestParam);//nqma vyvedeni nikakvi imena na universiteti
			if (uniIds == null && noUniNames && countryId == null) {
				continue;
			}

			if (uniIds == null && noUniNames) {
				_countryIds.add(countryId);
			} else {
				containsData = true;
				if (uniIds != null) {
					universityIds.addAll(uniIds);
				}
				addUniversityIds(universityDataProvider, countryId, uniNamesRequestParam, uniNameRequestParam, _universityIds, UniversityDataProvider.NAME_TYPE_BG);
				addUniversityIds(universityDataProvider, countryId, orgUniNamesRequestParam, orgUniNameRequestParam, _universityIds, UniversityDataProvider.NAME_TYPE_ORIGINAL);
			}
			
		}
		universityIds.addAll(_universityIds);
		countryIds.addAll(_countryIds);
		return containsData;
	}
	private static void addUniversityIds(UniversityDataProvider universityDataProvider, Integer countryId, String uniNamesParamValue, String uniNameParamValue, Set<Integer> universityIds, int nameType) {
		String[] orgUniNamesArr = StringUtils.isEmpty(uniNamesParamValue) ? null : uniNamesParamValue.split(";");
		List<String> uniNames = orgUniNamesArr == null || orgUniNamesArr.length == 0 ? new ArrayList<String>() : Arrays.asList(orgUniNamesArr);
		if (!StringUtils.isEmpty(uniNameParamValue)) {
			uniNames.add(uniNameParamValue);
		}
		if (uniNames.size() > 0) {
			for (String name:uniNames) {
				boolean startsWith = !name.startsWith("*");
				if (!startsWith) {
					name = name.substring(1);
				}
				if (name.endsWith("*")) {
					name = name.substring(0, name.length() - 1);
				}
				List<Integer> uniNamesIds = universityDataProvider.getUniversityIds(countryId, nameType, startsWith, name);
				if (uniNamesIds != null) {
					universityIds.addAll(uniNamesIds);
				}
			}
		}
	}


	/**
	 * generira universityCountryIds i universityIds ot request-a.
	 * @param request
	 * @param institutionIds
	 * @param countryIds
	 * @param nacidDataProvider
	 *
	 * @return dali ima nqkakvi danni za universities - t.e. opredelq dali trqbva da se nulira universityIds ako size()-a mu e 0
	 */
	public static boolean generateInstitutionCountryAndInstitutionIds(HttpServletRequest request, Set<Integer> institutionIds, Set<Integer> countryIds, NacidDataProvider nacidDataProvider) {
		int institutionsCount = DataConverter.parseInt(request.getParameter("institutions_count"), 0);
		TrainingInstitutionDataProvider trainingInstitutionDataProvider = nacidDataProvider.getTrainingInstitutionDataProvider();
		boolean containsData = false;
		for (int i = 0; i < institutionsCount; i++) {
			List<Integer> instIds = generateElementList(request, "institution" + i + "Ids", "institution" + i + "Id");
			String institutionNamesRequestParam = DataConverter.parseString(request.getParameter("institution" + i + "NamesIds"),"");
			String[] instNamesArr = "".equals(institutionNamesRequestParam) ? null : institutionNamesRequestParam.split(";");
			List<String> instNames = instNamesArr == null || instNamesArr.length == 0 ? new ArrayList<String>() : Arrays.asList(instNamesArr);
			if (!StringUtils.isEmpty(request.getParameter("institution" + i))) {
				instNames.add(request.getParameter("institution" + i));
			}
			Integer countryId = DataConverter.parseInteger(request.getParameter("_institution" + i + "Country"), null);
			if (instIds == null && (instNames.size() == 0) && countryId == null) {
				continue;
			}
			if (instIds == null && (instNames.size() == 0)) {
				countryIds.add(countryId);
			} else {
				containsData = true;
				if (instIds != null) {
					institutionIds.addAll(instIds);
				}
				if (instNames.size() > 0) {
					for (String name:instNames) {
						boolean startsWith = !name.startsWith("*");
						if (!startsWith) {
							name = name.substring(1);
						}
						if (name.endsWith("*")) {
							name = name.substring(0, name.length() - 1);
						}
						List<Integer> instNamesIds = trainingInstitutionDataProvider.getInstitutionIds(countryId, startsWith, name);
						if (instNamesIds != null) {
							institutionIds.addAll(instNamesIds);
						}

					}
				}
			}

		}
		return containsData;
	}
	
	
	
	
	/**
	 * 
	 * @param request
	 * @param nacidDataProvider
	 * @param type - diplomaSpeciality or recognizedSpeciality
	 * @return
	 */
	public static List<Integer> generateSpecialityIds(HttpServletRequest request, NacidDataProvider nacidDataProvider, String type) {
		Set<Integer> result = new HashSet<Integer>();
		int specialitiesCount = DataConverter.parseInt(request.getParameter(type + "_count"), 0);
		boolean hasData = false;
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		
		for (int i = 0; i < specialitiesCount; i++) {
			List<Integer> specIds = generateElementList(request, type + i + "Ids", type + i + "Id");
			String specialityNamesRequestParam = DataConverter.parseString(request.getParameter(type + i + "NamesIds"),"");
			String[] specNamesArr = "".equals(specialityNamesRequestParam) ? null : specialityNamesRequestParam.split(";");
			List<String> specNames = specNamesArr == null || specNamesArr.length == 0 ? new ArrayList<String>() : Arrays.asList(specNamesArr);
			if (!StringUtils.isEmpty(request.getParameter(type + i))) {
				specNames.add(request.getParameter(type + i));
			}
			Integer profGroupId = DataConverter.parseInteger(request.getParameter("_" + type + i + "ProfGroup"), null);
			if (specIds == null && (specNames.size() == 0) && profGroupId == null) {
				continue;
			}
			hasData = true;
			//Ako nqma vyvedena nikakva specialnost za tozi profession group, togava trqbva da se vzemat vsi4ki specialnosti ot tozi profGroup
			if (specIds == null && (specNames.size() == 0)) {
				List<Speciality> specs = nomenclaturesDataProvider.getSpecialities(profGroupId, null, null);
				if (specs != null) {
					result.addAll(getIds(specs));
				}
			} else {
				if (specIds != null) {
					result.addAll(specIds);
				}
				if (specNames.size() > 0) {
					for (String name:specNames) {
						boolean startsWith = !name.startsWith("*");
						if (!startsWith) {
							name = name.substring(1);
						}
						if (name.endsWith("*")) {
							name = name.substring(0, name.length() - 1);
						}
						List<Speciality> specs = nomenclaturesDataProvider.getSpecialities(name, startsWith, profGroupId, null, null);
						if (specs != null) {
							result.addAll(getIds(specs));
						}
						
					}
				}
			}
			
		}
		return !hasData && result.size() == 0 ? null : new ArrayList<Integer>(result);
	}
	private static List<Integer> getIds(List<Speciality> specs) {
		if (specs == null || specs.size() == 0) {
			return null;
		}
		List<Integer> result = new ArrayList<Integer>();
		for (Speciality s:specs) {
			result.add(s.getId());
		}
		return result;
		
	}
	@AllArgsConstructor
	private static class StringWithoutStars {
		String str;
		boolean startsWithStar;
	}
	private static StringWithoutStars generateStringWithoutStar(String name) {
		boolean startsWith = name.startsWith("*");
		if (startsWith) {
			name = name.substring(1);
		}
		if (name.endsWith("*")) {
			name = name.substring(0, name.length() - 1);
		}
		return new StringWithoutStars(name, startsWith);
	}
	public static CompanyApplicantRequest generateCompanyApplicantRequest(HttpServletRequest request, NacidDataProvider nacidDataProvider) {
		Set<Integer> result = new HashSet<>();
		List<Integer> companyIds = generateElementList(request, "companyIds", "companyId");
		if (!CollectionUtils.isEmpty(companyIds)) {
			result.addAll(companyIds);
		}

		String companyNamesRequestParam = DataConverter.parseString(request.getParameter("companyNamesIds"),"");
		String[] companyNamesArr = "".equals(companyNamesRequestParam) ? null : companyNamesRequestParam.split(";");
		if (companyNamesArr != null) {
			List<Integer> companyNameIds = Arrays.stream(companyNamesArr)
					.map(InquiresUtils::generateStringWithoutStar)
					.map(r -> nacidDataProvider.getCompanyDataProvider().getCompaniesByPartOfName(r.str, !r.startsWithStar))
					.filter(Objects::nonNull)
					.flatMap(r -> r.stream())
					.map(r -> r.getId())
					.collect(Collectors.toList());
			result.addAll(companyNameIds);
		}
		String[] ats = request.getParameterValues("companyApplicantType");
		List<Integer> applicantTypes = result.size() == 0 || ats == null || ats.length == 0 ? null : Arrays.stream(ats).map(r -> DataConverter.parseInteger(r, null)).filter(Objects::nonNull).collect(Collectors.toList());
		return result.size() == 0 ? null : new CompanyApplicantRequest(applicantTypes, new ArrayList<>(result));
//		return result.size() == 0 ? null : new ArrayList<>(result);
	}

	public static List<ApplicationTypeAndEntryNumSeries> generateApplicationTypeEntryNumSeries(HttpServletRequest request) {
		List<Integer> appTypes = generateElementList(request, "applicationTypeIds", "applicationType");
		List<ApplicationTypeAndEntryNumSeries> result = new ArrayList<>();
		if (appTypes != null) {
			for (Integer at : appTypes) {
				List<Integer> entryNums = null;
				String[] enParams = request.getParameterValues("entryNumSeries_" + at);
				if (enParams != null) {
					entryNums = Arrays.stream(enParams).map(r -> DataConverter.parseInteger(r, null)).filter(Objects::nonNull).collect(Collectors.toList());
				}
				int joinType = DataConverter.parseInt(request.getParameter("entryNumSeries_join_type_" + at), ApplicationTypeAndEntryNumSeries.JOIN_TYPE_AND);
				result.add(new ApplicationTypeAndEntryNumSeries(at, entryNums, joinType));

			}
		}
		return result;
	}
}
