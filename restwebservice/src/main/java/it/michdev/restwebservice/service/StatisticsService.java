package it.michdev.restwebservice.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import it.michdev.restwebservice.exception.DataNotFoundException;
import it.michdev.restwebservice.exception.InvalidStatsFieldException;
import it.michdev.restwebservice.model.DataPoint;
import it.michdev.restwebservice.model.HistoricalQuote;
import it.michdev.restwebservice.model.Report;
import it.michdev.restwebservice.model.dataseries.StatsSeries;
import it.michdev.restwebservice.model.dataseries.TimeSeries;
import it.michdev.restwebservice.utils.stats.StatisticalIndex;

/**
 * <b>StatisticsService</b> rappresenta un servizio dell'applicazione Spring per
 * l'elaborazione delle statistiche sul set di dati in ingresso.
 * 
 * @version 1.0.0
 * @author Michele Bevilacqua
 * @see it.michdev.restwebservice.utils.stats.StatisticIndex
 */
public final class StatisticsService {

    public static StatsSeries getCurrencyStats(String fieldName, TimeSeries timeSeries) throws InvalidStatsFieldException,
            DataNotFoundException {

        if (fieldName == null || fieldName.isEmpty())
            fieldName = "Close";

        LinkedHashMap<String, ArrayList<BigDecimal>> currencyValues = new LinkedHashMap<>();
        DataPoint firstDataPoint = timeSeries.getDataSeries().get(0);

        //inizializza la lista con i nomi delle valute e i rispettivi ArrayList<BigDecimal>
        if (!timeSeries.getDataSeries().isEmpty()) {
            for (HistoricalQuote hsQuote : firstDataPoint.getHistoricalQuote()) {
                currencyValues.put(hsQuote.getCurrencyPair(), new ArrayList<BigDecimal>());
            }
        } else
            throw new DataNotFoundException(
                    "La richiesta non ha prodotto nessun dato disponibile per le statistiche. Prova a cambiare parametri.");

        //Ricavo i dati 
        try {
            Method m = HistoricalQuote.class.getDeclaredMethod("get" + fieldName + "Value");
            for (DataPoint dataPoint : timeSeries.getDataSeries()) {
                for (HistoricalQuote hsQuote : dataPoint.getHistoricalQuote()) {
                    BigDecimal num = (BigDecimal) m.invoke(hsQuote);
                    currencyValues.get(hsQuote.getCurrencyPair()).add(num);
                }
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new InvalidStatsFieldException(
                    "Il campo{" + fieldName + "} non è disponibile. Consulta la documentazione.");
        }

        ArrayList<Report> reportList = new ArrayList<>();
        StatsSeries statsSeries = new StatsSeries(timeSeries.getPeriod());
        
        for (Map.Entry<String, ArrayList<BigDecimal>> value : currencyValues.entrySet()) {
            Report currencyReport = new Report(value.getKey());
            currencyReport.setAverage(StatisticalIndex.average(value.getValue()));
            currencyReport.setVariance(StatisticalIndex.variance(value.getValue()));
            currencyReport.setChange(StatisticalIndex.change(value.getValue().get(0),
                    value.getValue().get(value.getValue().size() - 1)));
            currencyReport.setPtcChange(StatisticalIndex.percentageChange(value.getValue().get(0), value.getValue().get(value.getValue().size() -1)));
            reportList.add(currencyReport);
        }
        statsSeries.setDataSeries(reportList);
        return statsSeries;
    }
}
