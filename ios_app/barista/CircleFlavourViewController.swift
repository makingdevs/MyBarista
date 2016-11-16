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
    var valueFlavors: [Double]!
    var flavors: [String]!
    var axisFormatterDelegate: IAxisValueFormatter!
    
    @IBOutlet weak var circleFlavorChart: RadarChartView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        axisFormatterDelegate = self
        flavors = ["Dulzura", "Acidez", "Florar", "Especiado", "Salado", "Frutos Rojos", "Chocolate", "Caramelo", "Cuerpo", "Limpieza"]
        if (circleFlavor == nil) {
            showCircleFlavor()
        } else {
            circleFlavorChart.noDataText = "Crea un círculo para tu bebida"
        }
    }
    
    /* Fetchs Check-in circle flavor */
    func showCircleFlavor() {
        CheckinManager.fetchCircleFlavor(
            circleFlavorId: checkin.circleFlavor!,
            onSuccess: {(circleFlavor: CircleFlavor) -> () in
                self.circleFlavor = circleFlavor
                self.setFlavorsInChart(circleFlavor: circleFlavor)
            },
            onError: {(error: String) -> () in
                print(error)
        })
    }
    
    func setFlavorsInChart(circleFlavor: CircleFlavor) {
        
        valueFlavors = [Double(circleFlavor.sweetness!), Double(circleFlavor.acidity!), Double(circleFlavor.flowery!), Double(circleFlavor.spicy!), Double(circleFlavor.salty!), Double(circleFlavor.berries!), Double(circleFlavor.chocolate!), Double(circleFlavor.candy!), Double(circleFlavor.body!), Double(circleFlavor.cleaning!)]
        
        let flavorRadar = valueFlavors.enumerated().map { x, y in return RadarChartDataEntry(value: y) }
        
        let data = RadarChartData()
        let dataChartSet = RadarChartDataSet(values: flavorRadar, label: "Sabores")
        dataChartSet.colors = [UIColor.brown]
        data.addDataSet(dataChartSet)
        self.circleFlavorChart.data = data
        self.circleFlavorChart.chartDescription?.text = ""
        
        let xaxis = circleFlavorChart.xAxis
        xaxis.valueFormatter = axisFormatterDelegate
    }
    
    func updateCircleFlavor(checkin: Checkin, circleFlavor: CircleFlavor) {
        self.checkin = checkin
        self.circleFlavor = circleFlavor
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let createCircleViewController = segue.destination as! CreateCircleFlavorViewController
        createCircleViewController.delegate = self
        createCircleViewController.checkin = self.checkin
        createCircleViewController.circleFlavor = self.circleFlavor
    }
    
    func stringForValue(_ value: Double, axis: AxisBase?) -> String {
        return flavors[Int(value) % flavors.count];
    }
}
