/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.GregorianCalendar;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.data.gantt.TaskSeriesCollection;

/**
 *
 * @author Dan Zimlich 2010
 */

public class DanChartMouseListener implements ChartMouseListener, MouseListener {

//====================FIELDS====================================================

private String categoryString = "";
private Rectangle2D rectangle;
private double mouseX;
private double mouseY;
private double startCoordinateDouble;

private ChartPanel chartPanel;

//===================CONSTRUCTORS===============================================



//===================METHODS====================================================

    private Point startPoint;
   private Point endPoint;

   @Override
    @SuppressWarnings("static-access")
   public void chartMouseClicked(ChartMouseEvent arg0) {
      
      ChartEntity entity = arg0.getEntity();

      JFreeChart chart = arg0.getChart();


//      System.out.println(entity.toString());

      if (entity.toString().startsWith("CategoryItemEntity")) {
         CategoryItemEntity catItem = (CategoryItemEntity) entity;
         MouseEvent event = arg0.getTrigger();
         
         TaskSeriesCollection dataset = (TaskSeriesCollection) catItem.getDataset();
         
         long endValueInt = dataset.getEndValue(0, 0, 0).longValue();
         GregorianCalendar calendar = new GregorianCalendar();
         calendar.setTimeInMillis(endValueInt);
         calendar.get(calendar.HOUR_OF_DAY);
         calendar.get(calendar.YEAR);
         
//         System.out.println("Dateline: "
//                            + calendar.get(calendar.HOUR_OF_DAY) + ":"
//                            + calendar.get(calendar.MINUTE) + ":"
//                            + calendar.get(calendar.SECOND) + "-"
//                            + calendar.get(calendar.YEAR) + "/"
//                            + (calendar.get(calendar.MONTH) + 1) + "/"
//                            + calendar.get(calendar.DATE));

//         System.out.println("Series: " + catItem.getSeries());
//         System.out.println("Category: "+ catItem.getCategory());
//         System.out.println("shapeCoords "+ catItem.getShapeCoords());
         String startCoordinate = catItem.getShapeCoords().substring(0,3);
         Double startCoordinateVal = Double.valueOf(startCoordinate);
         startCoordinateDouble = startCoordinateVal;
//         System.out.println("startCoord: "+startCoordinate);
         rectangle = (Rectangle2D) catItem.getArea();
//         System.out.println("columnKey "+ catItem.getColumnKey());
//         System.out.println("rowKey "+ catItem.getRowKey());

         
         
         
         this.categoryString = (String) catItem.getCategory();
         startPoint = event.getPoint();
         mouseX = startPoint.getX();
         mouseY = startPoint.getY();
    
         if (event.getButton() == MouseEvent.MOUSE_RELEASED) {
            endPoint = event.getPoint();
         }
         
//         System.out.println(startPoint);
//         System.out.println(endPoint);

         /**
          * HIGHLIGHT SELECTED BOX
          */
//         System.out.println();

      }      
   }

   @Override
   public void chartMouseMoved(ChartMouseEvent arg0) {
      //MouseEvent event = arg0.getTrigger();
      //System.out.println(event.getPoint().x + "/" + event.getPoint().y);
   }

   @Override
   public void mouseClicked(MouseEvent e) {
       
       
       
//      System.out.println("Clicked: " + e.getX() + "/" + e.getY());
   }

   @Override
   public void mouseEntered(MouseEvent e) {
      
   }

   @Override
   public void mouseExited(MouseEvent e) {
      
   }

   @Override
   public void mousePressed(MouseEvent e) {
//      System.out.println("Pressed: " + e.getX() + "/" + e.getY());
      
   }

   @Override
   public void mouseReleased(MouseEvent e) {
//      System.out.println("Release: " + e.getX() + "/" + e.getY());
      
   }

    public String getCategory() {
        return categoryString;
    }
    
    public Rectangle2D getRectangle2D(){
        return rectangle;
    }
    
    public double getX(){
        return mouseX;
    }
   
   public double getStartCoordinateDouble(){
       return startCoordinateDouble;
   }



}
