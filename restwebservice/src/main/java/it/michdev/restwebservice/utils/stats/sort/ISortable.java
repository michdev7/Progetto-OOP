package it.michdev.restwebservice.utils.stats.sort;

import java.math.BigDecimal;

/**
 * L'interfaccia <code>ISortable</code> definisce il comportamento delle classi che
 * rappresentano oggetti ordinabili in una rispettiva <code>Collection</code> in
 * base al valore della normale differenza tra valori e la differenza
 * percentuale.
 * 
 * @version 1.1.3
 * @author Michele Bevilacqua
 * @see it.michdev.restwebservice.utils.stats.sort.Sort
 */
public interface ISortable {

    /**
     * Restituisce il valore calcolato dell'indice statistico <i>change</i>.
     * 
     * @return <code>BigDecimal</code>
     */
    public BigDecimal getChangeValue();

    /**
     * Restituisce il valore calcolato dell'indice statistico <i>percentage
     * change</i>.
     * 
     * @return <code>BigDecimal</code>
     */
    public BigDecimal getPctChangeValue();

    /**
     * Restituisce il codice della coppia di valute elaborata.
     * 
     * @return <code>BigDecimal</code>
     */
    public String getCurrencyPair();
}
