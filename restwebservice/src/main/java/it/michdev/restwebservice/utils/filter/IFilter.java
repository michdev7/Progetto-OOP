package it.michdev.restwebservice.utils.filter;

/**
 * L'interfaccia <code>IFilter</code> definisce il comportamento delle classi
 * che rappresentano un un filtro di dati. Un filtro di dati contiene a sua
 * volta i parametri che vengono poi applicati dal FilterService per selezionare
 * i dati.
 * 
 * @version 1.1.3
 * @author Michele Bevilacqua
 * @see it.michdev.restwebservice.utils.filter.CurrencyFilter
 * @see it.michdev.restwebservice.utils.filter.PeriodFilter
 */
public interface IFilter<T> {

    /**
     * Ottiene il parametro del filtro istanziato.
     * 
     * @return oggetto di tipo parametrizzato <code>T</code>.
     */
    public abstract T getParam();
}
