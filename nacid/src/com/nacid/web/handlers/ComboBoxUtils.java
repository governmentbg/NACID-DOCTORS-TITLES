package com.nacid.web.handlers;

import com.nacid.bl.OrderCriteria;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.users.User;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.RadioButtonWebModel;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class ComboBoxUtils {

	/**
	 * 
	 * @param activeId
	 * @param nomenclatureType - edin ot definiranite v {@link NomenclaturesDataProvider}
	 * @param nomenclaturesDataProvider
	 * @param skipInactive - dali da skip-ne neaktivnite nomenklature
	 * @param request - ako e null ne slaga atribut v sesiqta
	 * @param comboName - ime na request atributa v koito se slagat nomenclaturite. Ako e null, ne slaga atribut v sesiqta
	 * @param orderCriteria - kriterii po koito shte sa sortirani nomenklaturite - null po podrazbirane (ako trqbva drug kriterii, se syzdava s {@link OrderCriteria}.createOrderCriteria()) 
	 * @param addEmpty - dali da dobavq prazen red
	 * @return
	 */
	public static ComboBoxWebModel generateNomenclaturesComboBox(Integer activeId, int nomenclatureType, NomenclaturesDataProvider nomenclaturesDataProvider, boolean skipInactive, HttpServletRequest request, String comboName, OrderCriteria orderCriteria, boolean addEmpty) {
		List<? extends FlatNomenclature> flatNomeclatures = null;
		if (nomenclatureType == NomenclaturesDataProvider.NOMENCLATURE_COUNTRY) {
			flatNomeclatures = nomenclaturesDataProvider.getCountries(null, orderCriteria);
		} else if (nomenclatureType == NomenclaturesDataProvider.NOMENCLATURE_PROFESSION_GROUP){
			flatNomeclatures = nomenclaturesDataProvider.getProfessionGroups(0, null, orderCriteria);
		} else if (nomenclatureType == NomenclaturesDataProvider.NOMENCLATURE_SPECIALITY) {
			flatNomeclatures = nomenclaturesDataProvider.getSpecialities(0, null, orderCriteria);
		} else if (nomenclatureType == NomenclaturesDataProvider.NOMENCLATURE_DOCUMENT_TYPE) {
			flatNomeclatures = nomenclaturesDataProvider.getDocumentTypes(null, orderCriteria, 0);
		} /*else if (nomenclatureType == NomenclaturesDataProvider.NOMENCLATURE_LEGAL_REASON) {
			flatNomeclatures = nomenclaturesDataProvider.getLegalReasons(null, orderCriteria, null);
		} */else if (nomenclatureType == NomenclaturesDataProvider.NOMENCLATURE_EXPERT_POSITION) {
            flatNomeclatures = nomenclaturesDataProvider.getExpertPositions();
        } else if (nomenclatureType == NomenclaturesDataProvider.NOMENCLATURE_SECONDARY_PROFESSIONAL_QUALIFICATION) {
		    flatNomeclatures = nomenclaturesDataProvider.getSecondaryProfessionalQualifications(null, orderCriteria, null);
		} else if (nomenclatureType == NomenclaturesDataProvider.NOMENCLATURE_APPLICATION_STATUS) {
            flatNomeclatures = nomenclaturesDataProvider.getApplicationStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, orderCriteria, false);
        } else if (nomenclatureType == NomenclaturesDataProvider.NOMENCLATURE_APPLICATION_DOCFLOW_STATUS) {
            flatNomeclatures = nomenclaturesDataProvider.getApplicationDocflowStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, orderCriteria);
        }  else if (nomenclatureType == NomenclaturesDataProvider.NOMENCLATURE_ORIGINAL_EDUCATION_LEVEL) {
            flatNomeclatures = nomenclaturesDataProvider.getOriginalEducationLevels(null, orderCriteria);
        } else if (nomenclatureType == NomenclaturesDataProvider.NOMENCLATURE_LANGUAGE) {
			flatNomeclatures = nomenclaturesDataProvider.getLanguages(null, orderCriteria);
		} else if (nomenclatureType == NomenclaturesDataProvider.NOMENCLATURE_DOCUMENT_RECEIVE_METHOD) {
			flatNomeclatures = nomenclaturesDataProvider.getDocumentReceiveMethods(null, orderCriteria);
		} else {
			flatNomeclatures = nomenclaturesDataProvider.getFlatNomenclatures(nomenclatureType, null, orderCriteria);
		}
		return generateNomenclaturesComboBox(activeId, flatNomeclatures, skipInactive, request, comboName, addEmpty);

	}
	/**
	 * generira NomenclaturesComboBox i go vry6ta
	 * ako comboName == null ili request == null,  togava ne slaga atribut v sesiqta
	 * @param activeId
	 * @param nomenclatures
	 * @param skipInactive
	 * @param request
	 * @param comboName
	 * @return
	 */
	public static ComboBoxWebModel generateNomenclaturesComboBox(Integer activeId, List<? extends FlatNomenclature> nomenclatures, boolean skipInactive, HttpServletRequest request, String comboName, boolean addEmpty) {
		ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", addEmpty);
		if (nomenclatures != null) {
			for (FlatNomenclature s : nomenclatures) {
				boolean isActive = s.isActive();
				if (skipInactive && !isActive && (activeId == null || (activeId != null && activeId != s.getId()))) {
					continue;
				}
				combobox.addItem(s.getId() + "", getValue(s) + (isActive ? "": "(inactive)"));
			}
		}
        if (comboName != null && request != null) {
            request.setAttribute(comboName, combobox);
        }
		return combobox;
	}

	public static ComboBoxWebModel generateProfessionGroupComboBox(Integer activeId, List<ProfessionGroup> nomenclatures, boolean skipInactive, HttpServletRequest request, String comboName, boolean addEmpty) {
		ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", addEmpty);
		if (nomenclatures != null) {
			for (ProfessionGroup s : nomenclatures) {
				boolean isActive = s.isActive();
				if (skipInactive && !isActive && (activeId == null || (activeId != null && activeId != s.getId()))) {
					continue;
				}
				Map<String, String> additionalAttributes = new HashMap<>();
				additionalAttributes.put("attr-edu-area-id", s.getEducationAreaId() + "");
//				additionalAttributes.put("attr-edu-area-name", s.getEducationAreaName() + "");
				combobox.addItem(s.getId() + "", getValue(s) + (isActive ? "": "(inactive)"), additionalAttributes);
			}
		}
		if (comboName != null && request != null) {
			request.setAttribute(comboName, combobox);
		}
		return combobox;
	}

	public static RadioButtonWebModel generateDocumentReceiveMethodRadioButton(Integer activeId, List<DocumentReceiveMethod> nomenclatures, boolean skipInactive, HttpServletRequest request, String comboName) {
		Function<FlatNomenclature, Map<String, String>> generator = (FlatNomenclature n) -> {
			Map<String, String> additionalAttributes = new HashMap<>();
			additionalAttributes.put("attr-has-document-recipient", ((DocumentReceiveMethod)n).hasDocumentRecipient() + "");
			return additionalAttributes;
		};
		return _generateNomenclaturesRadioButton(activeId, nomenclatures, skipInactive, request, comboName, generator);
	}
	
	public static RadioButtonWebModel generateNomenclaturesRadioButton(Integer activeId, List<? extends FlatNomenclature> nomenclatures, boolean skipInactive, HttpServletRequest request, String comboName) {
		return _generateNomenclaturesRadioButton(activeId, nomenclatures, skipInactive, request, comboName, (nom) -> null);
    }

	private static RadioButtonWebModel _generateNomenclaturesRadioButton(Integer activeId, List<? extends FlatNomenclature> nomenclatures, boolean skipInactive, HttpServletRequest request, String comboName, Function<FlatNomenclature, Map<String, String>> additionalArgsGenerator) {
		RadioButtonWebModel combobox = new RadioButtonWebModel(activeId == null ? "" : activeId + "");
		if (nomenclatures != null) {
			for (FlatNomenclature s : nomenclatures) {
				boolean active = s.isActive();
				if (skipInactive && !active && (!Objects.equals(activeId, s.getId()))) {
					continue;
				}
				Map<String, String> additionalArgs = additionalArgsGenerator.apply(s);
				if (additionalArgs != null) {
					combobox.addItem(s.getId() + "", getValue(s) + (active ? "": "(inactive)"), additionalArgs);
				} else {
					combobox.addItem(s.getId() + "", getValue(s) + (active ? "": "(inactive)"));
				}

			}
			if (comboName != null && request != null) {
				request.setAttribute(comboName, combobox);
			}

		}
		return combobox;
	}
	
	public static ComboBoxWebModel generateUsersComboBox(Integer activeId, List<? extends User> users, HttpServletRequest request, String comboName, boolean addEmpty, boolean addInactive) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", addEmpty);
        if (users != null) {
            for (User s : users) {
                if (addInactive || s.getStatus() == 1) {
                    combobox.addItem(s.getUserId() + "", s.getFullName());    
                }
            }
            if (comboName != null && request != null) {
                request.setAttribute(comboName, combobox);  
            }

        }
        return combobox;
    }
	
	
	
	public static ComboBoxWebModel generateExpertsCombobox(Integer activeId, List<ComissionMember> members, HttpServletRequest request, String comboName, boolean addEmpty) {
	    ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", addEmpty);
        if (members != null) {
            for (ComissionMember s : members) {
                combobox.addItem(s.getFullName(), s.getFullName());
            }
            if (comboName != null && request != null) {
                request.setAttribute(comboName, combobox);  
            }

        }
        return combobox;
	}
	
	private static String getValue(FlatNomenclature n) {
		if (n instanceof DocumentType) {
			return 	((DocumentType)n).getLongName();
		}
		return n.getName();
	}

	/**
	 * @param activeId
	 * @param objects - vsqkakyv tip - moje i da ne e nomenclatura!
	 * @param request
	 * @param requestAttributeName
	 * @param addEmpty
	 * @param keyName - imeto na getter-a kojto trqbva da se izvika ot object-a za da se popylnqt stojnostite na key-a! - cqloto ime na getter-a (vkliuchitelno get - primer getId)
	 * @param valueName - imeto na getter-a, kojto trqbva da seizvika za da se popylnqt stojnostite na value-to! - cqloto ime na gettera - primerno getName!
	 * @return
	 */
	   public static ComboBoxWebModel generateComboBox(Integer activeId, List<?> objects, HttpServletRequest request, String requestAttributeName, boolean addEmpty, String keyName, String valueName) {
	        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", addEmpty);
	        if (objects != null) {
	            for (Object s : objects) {
	                try {
                        combobox.addItem(s.getClass().getMethod(keyName).invoke(s).toString(), s.getClass().getMethod(valueName).invoke(s).toString());
                    } catch (IllegalArgumentException e) {
                        throw Utils.logException(e);
                    } catch (SecurityException e) {
                        throw Utils.logException(e);
                    } catch (IllegalAccessException e) {
                        throw Utils.logException(e);
                    } catch (InvocationTargetException e) {
                        throw Utils.logException(e);
                    } catch (NoSuchMethodException e) {
                        throw Utils.logException(e);
                    }
	            }
	            if (requestAttributeName != null && request != null) {
	                request.setAttribute(requestAttributeName, combobox);  
	            }

	        }
	        return combobox;
	    }
}
