/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author Dan Zimlich 2010
 */

public class DanGanttRenderer extends GanttRenderer{

//====================FIELDS====================================================

      private transient Paint completePaint;
      private transient Paint incompletePaint;
      private double startPercent;
      private double endPercent;

      private int selectedRow =         -1;
      private int selectedColumn =      -1;
      private int selectedSubinterval = -1;

      private int subintervalStartDate = 0;
      private int subintervalStartHour =    0;
      private int subintervalStartMin =     0;
      private int subintervalPatientID =    -1;

      private ParameterProcessor parameterProcessor;

//===================CONSTRUCTORS===============================================

      public DanGanttRenderer(ParameterProcessor parameterProcessor) {
         super();
         setIncludeBaseInRange(false);
         this.completePaint = Color.green;
         this.incompletePaint = Color.red;
         this.startPercent = 0.35;
         this.endPercent = 0.65;

         this.parameterProcessor = parameterProcessor;


      }



//===================METHODS====================================================
    @Override
    @SuppressWarnings("static-access")
      protected void drawTasks(Graphics2D g2,
            CategoryItemRendererState state,
            Rectangle2D dataArea,
            CategoryPlot plot,
            CategoryAxis domainAxis,
            ValueAxis rangeAxis,
            GanttCategoryDataset dataset,
            int row,
            int column) 
    {

         int count = dataset.getSubIntervalCount(row, column);
//         System.out.println("The subintervalCount for"+ row+" col: "+column +" is in DanGanttRenderer is: "+count);
         if (count == 0) {
            drawTask(g2, state, dataArea, plot, domainAxis, rangeAxis,
                  dataset, row, column);
         }

         for (int subinterval = 0; subinterval < count; subinterval++) {

//             System.out.println("row: "+row+" col: "+column+" subint: "+subinterval);

            RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();


            //============Check to see if times are registered to subtasks
         Calendar calendar = new GregorianCalendar();
         calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)-1900);


            // value 0
            Number value0 = dataset.getStartValue(row, column, subinterval);
            Long startTime = (Long) value0;
            calendar.setTimeInMillis(startTime);

            
            subintervalStartDate = calendar.get(calendar.DATE);
            subintervalStartHour = calendar.get(calendar.HOUR_OF_DAY);
            subintervalStartMin = calendar.get(calendar.MINUTE);

//            System.out.println("subintervalTimes: "+calendar.getTime().toString()+" year is..: "+calendar.get(Calendar.YEAR));

            int subintervalPatientIndex = column;
            subintervalPatientID = parameterProcessor.getPatientList().get(subintervalPatientIndex).getID();
            
            if (value0 == null) {
               return;
            }
            double translatedValue0 = rangeAxis.valueToJava2D(
                  value0.doubleValue(), dataArea, rangeAxisLocation);

            // value 1
            Number value1 = dataset.getEndValue(row, column, subinterval);
//            System.out.println("endVal: " + value1);
            if (value1 == null) {
               return;
            }
            double translatedValue1 = rangeAxis.valueToJava2D(
                  value1.doubleValue(), dataArea, rangeAxisLocation);

            if (translatedValue1 < translatedValue0) {
               double temp = translatedValue1;
               translatedValue1 = translatedValue0;
               translatedValue0 = temp;
            }

            double rectStart = calculateBarW0(plot, plot.getOrientation(),
                  dataArea, domainAxis, state, row, column);
            double rectLength = Math.abs(translatedValue1 - translatedValue0);
            double rectBreadth = state.getBarWidth();

            // DRAW THE BARS...
            Rectangle2D bar = null;

            if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
               bar = new Rectangle2D.Double(translatedValue0, rectStart,
                     rectLength, rectBreadth);
            }
            else if (plot.getOrientation() == PlotOrientation.VERTICAL) {
               bar = new Rectangle2D.Double(rectStart, translatedValue0,
                     rectBreadth, rectLength);
            }

            Rectangle2D completeBar = null;
            Rectangle2D incompleteBar = null;
            Number percent = dataset.getPercentComplete(row, column,
                  subinterval);
            double start = getStartPercent();
            double end = getEndPercent();
            if (percent != null) {
               double p = percent.doubleValue();
               if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
                  completeBar = new Rectangle2D.Double(translatedValue0,
                        rectStart + start * rectBreadth, rectLength * p,
                        rectBreadth * (end - start));
                  incompleteBar = new Rectangle2D.Double(translatedValue0
                        + rectLength * p, rectStart + start * rectBreadth,
                        rectLength * (1 - p), rectBreadth * (end - start));
               }
               else if (plot.getOrientation() == PlotOrientation.VERTICAL) {
                  completeBar = new Rectangle2D.Double(rectStart + start
                        * rectBreadth, translatedValue0 + rectLength
                        * (1 - p), rectBreadth * (end - start),
                        rectLength * p);
                  incompleteBar = new Rectangle2D.Double(rectStart + start
                        * rectBreadth, translatedValue0, rectBreadth
                        * (end - start), rectLength * (1 - p));
               }

            }

            /**
             * PAINTING
             */
            Paint seriesPaint = getItemPaint(row, column, subinterval);
//            g2.setPaint(seriesPaint);
//            System.out.println("paint subtask");

//            System.out.println("subintPatID: "+subintervalPatientID+" subintStartHour:"+subintervalStartHour+" subintStartMin:"+subintervalStartMin);
            int subintervalOpID = -1;
            for(Object opObject : parameterProcessor.getOperationList()){
                Operation operation = (Operation) opObject;

                /**
                 * TEST PRINTING
                 */
                if(     (operation.getPatientID() == subintervalPatientID)&&
                        (operation.getStartDate() == subintervalStartDate) &&
                        (operation.getStartHour() == subintervalStartHour) &&
                        (operation.getStartMinute() == subintervalStartMin)){
                    subintervalOpID = operation.getID();
                }
            }
//            System.out.println("subintID: "+subintervalOpID);
            if(subintervalOpID != -1){
//                System.out.println("hit paint switch");
                switch (parameterProcessor.getOperationList().getByID(subintervalOpID).getResourceID()){

                    case 0:
                        g2.setPaint(new Color(255,0,0));
//                        System.out.println("Painted Resource 0 Red");
                        break;
                    case 1:
                        g2.setPaint(new Color(0,255,0));
                        break;
                    case 2:
                        g2.setPaint(new Color(0,0,255));
                        break;
                    case 3:
                        g2.setPaint(new Color(255,255,0));
                        break;
                    case 4:
                        g2.setPaint(new Color(0,255,255));
                        break;
                    case 5:
                        g2.setPaint(new Color(255,0,255));
                        break;
                    case 6:
                        g2.setPaint(new Color(134,93,13));
                        break;
                    case 7:
                        g2.setPaint(new Color(102,45,145));
                        break;
                    case 8:
                        g2.setPaint(new Color(255,147,0));
                        break;
                    case 9:
                        g2.setPaint(new Color(0,0,0));
                        break;

                    default:
                        System.out.println("subintervalOpID was not found..: "+ subintervalOpID);
                        g2.setPaint(Color.BLACK);
                        break;

                }
            }
            
            
            g2.fill(bar);

            if (completeBar != null) {
               g2.setPaint(getCompletePaint());
               g2.fill(completeBar);
            }
            if (incompleteBar != null) {
               g2.setPaint(getIncompletePaint());
               g2.fill(incompleteBar);
            }
            if (isDrawBarOutline()
                  && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
               g2.setStroke(getItemStroke(row, column));
               g2.setPaint(getItemOutlinePaint(row, column));
               g2.draw(bar);
            }

            /**
             * The following code was added to extend the GanttRenderer class spec'd by the JFreeChart API
             * It allows subtasks to retain a unique label.
             */
            CategoryItemLabelGenerator generator = getItemLabelGenerator(row, column);
            if (generator != null && isItemLabelVisible(row, column)) {
               drawItemLabel(g2, dataset, row, column, plot, generator, bar, false);
            }

            // collect entity and tool tip information...
            if (state.getInfo() != null) {
               EntityCollection entities = state.getEntityCollection();
               if (entities != null) {
                  String tip = null;
                  if (getToolTipGenerator(row, column) != null) {
                     tip = getToolTipGenerator(row, column).generateToolTip(
                           dataset, row, column);
                  }
                  String url = null;
                  if (getItemURLGenerator(row, column) != null) {
                     url = getItemURLGenerator(row, column).generateURL(
                           dataset, row, column);
                  }
                  CategoryItemEntity entity = new CategoryItemEntity(
                        bar, tip, url, dataset, dataset.getRowKey(row),
                        dataset.getColumnKey(column));
                  entities.add(entity);
               }
            }
         }
      }

    
   public Paint getItemPaint(int row, int column, int subinterval) {

      Paint p = super.getItemPaint(row, column);

      /**
       * HIGHLIGHT selected item
       */
      if(row==selectedRow && column==selectedColumn && subinterval  == selectedSubinterval){
         p = Color.YELLOW;
      }
      return p;
//      return DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE[column];
   }



    public int getSelectedColumn() {
      return selectedColumn;
   }
   public void setSelectedColumn(int selectedColumn) {
      this.selectedColumn = selectedColumn;
   }
   public int getSelectedRow() {
      return selectedRow;
   }
   public void setSelectedRow(int selectedRow) {
      this.selectedRow = selectedRow;
   }

   public int getSelectedSubinterval() {
      return selectedSubinterval;
   }
   public void setSelectedSubinterval(int selectedSubinterval) {
      this.selectedSubinterval = selectedSubinterval;
   }

}
