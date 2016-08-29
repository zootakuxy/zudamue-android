package com.st.dbutil.android.model;

import java.io.Serializable;

public interface ListNavegation
{
	
	public static enum Navegation implements Serializable
	{
		/**
		 * Navegar para frente
		 */
		GO_FRONT,
		
		/**
		 * Navegar para traz
		 */
		GO_BACK,
		
		/**
		 * Navegar para cima
		 */
		GO_TOP,
		
		/**
		 * Navegar para baixo
		 */
		GO_BUTTOM,
		
		/**
		 * Navegar para a direita
		 */
		GO_RITHG,
		
		/**
		 * Navegar para esquerda
		 */
		GO_LEFT,
		
		/**
		 * Navegar para o primeiro
		 */
		GO_FRIST,
		
		/**
		 * Navegar para a ultima
		 */
		GO_LAST
	}
	
	/**
	 * Navegar para uma dada direcao
	 * @param navegation
	 */
	public void naveTo(Navegation navegation);
	
	
	/**
	 * Navegar para uma dada area
	 * @param keyLoacal
	 */
	public void naveTo(String keyLoacal);
	
	/**
	 * Navegar para uma dada posicao do item
	 * @param index
	 */
	public void naveTo(int index);
	
}
