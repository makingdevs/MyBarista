//
//  CircleFlavourViewController.swift
//  barista
//
//  Created by MakingDevs on 14/11/16.
//  Copyright © 2016 MakingDevs. All rights reserved.
//

import UIKit
import Charts

class CircleFlavourViewController: UIViewController, CircleFlavorDelegate, IAxisValueFormatter {

    var checkin: Checkin!
    var circleFlavor: CircleFlavor!
    var axisFormatterDelegate: IAxisValueFormatter!
    @IBOutlet weak var imageCircle: UIView!
    
    @IBOutlet weak var circleFlavorChart: RadarChartView!
    
  @IBOutlet weak var circleButton: UIButton!
  override func viewDidLoad() {
    super.viewDidLoad()
    axisFormatterDelegate = self
    
    if (circleFlavor == nil) {
      circleFlavorChart.noDataText = ""
      showCircleFlavor()
    }
    circleFlavorChart.sizeToFit()
    
    circleButton.bordered()
    
  }
    
    /* Fetchs Check-in circle flavor */
    func showCircleFlavor() {
        CheckinManager.fetchCircleFlavor(
            circleFlavorId: checkin.circleFlavor!,
            onSuccess: {(circleFlavor: CircleFlavor) -> () in
                self.circleFlavor = circleFlavor
                self.circleButton.titleLabel?.text = "EDITAR CÍRCULO DE SABOR"
                self.setFlavorsInChart(circleFlavor: circleFlavor)
            },
            onError: {(error: String) -> () in
                print(error)
        })
    }
    
    func setFlavorsInChart(circleFlavor: CircleFlavor) {
        self.imageCircle.isHidden = true
        
        let valueFlavors = [Double(circleFlavor.sweetness!),
                        Double(circleFlavor.acidity!),
                        Double(circleFlavor.flowery!),
                        Double(circleFlavor.spicy!),
                        Double(circleFlavor.salty!),
                        Double(circleFlavor.berries!),
                        Double(circleFlavor.chocolate!),
                        Double(circleFlavor.candy!),
                        Double(circleFlavor.body!),
                        Double(circleFlavor.cleaning!)]
        
        let flavorRadar = valueFlavors.enumerated().map { x, y in return RadarChartDataEntry(value: y) }
        
        let data = RadarChartData()
        let dataChartSet = RadarChartDataSet(values: flavorRadar, label: "")
        dataChartSet.colors = [UIColor.brown]
        dataChartSet.drawFilledEnabled = true
        dataChartSet.drawIconsEnabled = false
        dataChartSet.drawHighlightCircleEnabled = false
        dataChartSet.drawVerticalHighlightIndicatorEnabled = false
        dataChartSet.fillColor = .lightGray
        
        data.addDataSet(dataChartSet)
        self.circleFlavorChart.data = data
        self.circleFlavorChart.drawWeb = false
        let xaxis = circleFlavorChart.xAxis
        xaxis.valueFormatter = axisFormatterDelegate
        
        self.circleFlavorChart.legend.enabled = false
        self.circleFlavorChart.chartDescription?.text = ""
        
    }
    
    func updateCircleFlavor(checkin: Checkin, circleFlavor: CircleFlavor) {
        self.checkin = checkin
        self.circleFlavor = circleFlavor
        circleButton.titleLabel?.text = "EDITAR CÍRCULO DE SABOR"
        self.setFlavorsInChart(circleFlavor: circleFlavor)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let createCircleViewController = segue.destination as! CreateCircleFlavorViewController
        createCircleViewController.circleFlavorDelegate = self
        createCircleViewController.checkin = self.checkin
        createCircleViewController.circleFlavor = self.circleFlavor
    }
    
    func stringForValue(_ value: Double, axis: AxisBase?) -> String {
        let flavors = ["Dulzura", "Acidez", "Florar", "Especiado", "Salado", "Frutos Rojos", "Chocolate", "Caramelo", "Cuerpo", "Limpieza"]
        return flavors[Int(value) % flavors.count];
    }
}
