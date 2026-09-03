import { AfterViewInit, Component, ElementRef, Input, OnChanges, OnDestroy, SimpleChanges, ViewChild } from '@angular/core';
import * as echarts from 'echarts';
@Component({selector:'app-mini-chart',standalone:true,template:'<div #host class="chart" role="img" [attr.aria-label]="title"></div>',styles:['.chart{height:230px;width:100%}']})
export class MiniChartComponent implements AfterViewInit,OnChanges,OnDestroy {
 @Input() title='Chart'; @Input() labels:string[]=[]; @Input() series:{name:string;data:number[]}[]=[]; @ViewChild('host') host?:ElementRef<HTMLDivElement>; private chart?:echarts.ECharts;
 ngAfterViewInit(){this.chart=echarts.init(this.host!.nativeElement);this.render();}
 ngOnChanges(_:SimpleChanges){this.render();} ngOnDestroy(){this.chart?.dispose();}
 private render(){if(!this.chart)return;this.chart.setOption({backgroundColor:'transparent',tooltip:{trigger:'axis'},legend:{textStyle:{color:'#91a6b8'}},grid:{left:44,right:12,top:42,bottom:34},xAxis:{type:'category',data:this.labels,axisLabel:{color:'#7890a4'},axisLine:{lineStyle:{color:'#294052'}}},yAxis:{type:'value',axisLabel:{color:'#7890a4'},splitLine:{lineStyle:{color:'#203344'}}},series:this.series.map((s,i)=>({...s,type:'line',smooth:true,symbolSize:7,lineStyle:{width:3,color:i?'#f4bd61':'#49e0b5'},itemStyle:{color:i?'#f4bd61':'#49e0b5'},areaStyle:i?undefined:{color:'#49e0b515'}}))});}
}
