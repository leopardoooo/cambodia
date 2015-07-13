/***
 * The Factory is final, create charts by HighCharts, support spline, pie, bar 
 */
 ChartFactory = function(){
 	function pieFormatter(){
 		return '<b>'+ this.point.name +'</b>: '+ this.percentage.toFixed(2) +' %';
 	}
 	//chart's differentiation
 	var DEFAULT_OPTIONS = {
 		//pie chart's config
 		pie: {
 			plotOptions: {
 				pie: {
					allowPointSelect: true,
					cursor: 'pointer',
					dataLabels: { enabled: true, color: '#000000', connectorColor: '#000000', formatter: pieFormatter }
				}
 			},
 			tooltip: { 
 				formatter: pieFormatter 
 			}
 		},
 		//line chart's config
		spline: { 
			plotOptions: {
				spline: {
					marker: {radius: 4, lineColor: '#666666',  lineWidth: 1  } 
				}
			},
			tooltip: {
				crosshairs: true, 
				shared: true 
			}
		},
		//bar chart's config
		column: {
			plotOptions: {
				column: {pointPadding: 0.2, borderWidth: 0 }
			},
			tooltip: {
				shared: true 
			}
		}
 	}
 	//inner class integration highcharts
 	var F = Ext.extend(Ext.util.Observable, {
 		//public properties
 		type: null,
 		title: null,
 		subtitle: null,
 		xtitle: null,
 		ytitle: null,
 		categories: null,
 		series: null,
 		selected: 0,
 		
 		//private properties
 		plotOps: null,
 		
 		constructor: function(options){
 			Ext.apply(this, options);
 			F.superclass.constructor.call(this);
 			this.initialize();
 		},
 		initialize: function(){
 			if(!this.type){
 				var TYPES = ['column', 'spline', 'pie'];
 				this.type = TYPES[Math.round(Math.random() * 10 % 2)];
 			}
 			
 			// if type is pie, the series need to format
 			if(this.type === 'pie'){
 				var pieData = [], sArr = this.series;
 				for(var i = 0; i< sArr.length; i++){
 					pieData.push([sArr[i].name, sArr[i]["data"][0]]);
 				}
 				if(pieData[this.selected]){
 					var arr = pieData[this.selected];
 					pieData[this.selected] = {
 						name: arr[0],
 						y: arr[1],
 						sliced: true,
						selected: true
 					}
 				}
 				this.series = [{ 
 					data: pieData
 				}]
 			}
 		},
 		render: function(elementId){
 			var options = DEFAULT_OPTIONS[this.type];
 			Ext.apply(options, {
				chart: {
					renderTo: elementId, 
					type: this.type 
				},
				title: {text: this.title },
				subtitle: {text: this.subtitle },
				xAxis: {
					title: {
						text: this.xtitle 
					}, 
					categories: this.categories 
				},
				yAxis: {
					min: this.min || 0, 
					title: { 
						text: this.ytitle 
					} 
				},
				series: this.series,
				credits: {
					enabled: false 
				},
				exporting:{
					enabled: false,
				    filename:'chart',
				    url: root + "/chart.action"
				}
 			});
 			// create highchart's chart
 			this.chart = new Highcharts.Chart(options);
 		},
 		exportChart: function(){
 			this.chart.exportChart({
				type: 'image/jpeg'
			});
 		}
 	});
 	
 	var CF = {
 		createChart: function(options){
 			return new F(options);
 		}
 	}
 	return CF;
 }();