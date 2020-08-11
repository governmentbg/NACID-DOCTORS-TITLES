package com.nacid.bl.nomenclatures;


public interface CopyType extends FlatNomenclature{
	/**
	 * kopie
	 */
	public static final int COPY_TYPE_COPY = 1;

	/**
	 * original
	 */
	public static final int COPY_TYPE_ORIGINAL_DOC = 2;

	/**
	 * prevod
	 */
	public static final int COPY_TYPE_TRANSLATED_DOC = 3;
	/**
	 * elektronno
	 */
	public static final int ELECTRONIC_FORM = 5;
}
