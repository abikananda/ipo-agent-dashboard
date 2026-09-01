import { DatePipe, DecimalPipe, KeyValuePipe, NgClass } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IpoDetail, IpoSummary } from './models';
import { MiniChartComponent } from './mini-chart.component';

@Component({selector:'app-root',standalone:true,imports:[DatePipe,DecimalPipe,KeyValuePipe,NgClass,FormsModule,MiniChartComponent],templateUrl:'./app.component.html'})
export class AppComponent {
 private http=inject(HttpClient); ipos=signal<IpoSummary[]>([]); loading=signal(true); error=signal(''); query=signal(''); status=signal('ALL'); selected=signal<IpoSummary|null>(null); detail=signal<IpoDetail|null>(null); detailLoading=signal(false); compareIds=signal<number[]>([]);
 filtered=computed(()=>this.ipos().filter(x=>(this.status()==='ALL'||x.ipo.status===this.status())&&x.ipo.companyName.toLowerCase().includes(this.query().toLowerCase())));
 openCount=computed(()=>this.ipos().filter(x=>x.ipo.status==='OPEN').length);
 averageScore=computed(()=>this.ipos().length?Math.round(this.ipos().reduce((a,b)=>a+b.analysis.overallScore,0)/this.ipos().length):0);
 constructor(){this.http.get<IpoSummary[]>('/api/v1/ipos').subscribe({next:v=>{this.ipos.set(v);this.loading.set(false)},error:()=>{this.error.set('The API is unavailable. Start the Spring Boot service and refresh.');this.loading.set(false)}})}
  badge(v:string){return v==='APPLY'?'positive':v==='APPLY_WITH_CAUTION'?'caution':v==='AVOID'?'negative':'muted'}
 open(item:IpoSummary){this.selected.set(item);this.detail.set(null);this.detailLoading.set(true);this.http.get<IpoDetail>(`/api/v1/ipos/${item.ipo.slug}`).subscribe({next:d=>{this.detail.set(d);this.detailLoading.set(false)},error:()=>this.detailLoading.set(false)});}
 close(){this.selected.set(null);this.detail.set(null)}
 toggleCompare(id:number,event:Event){event.stopPropagation();const ids=this.compareIds();this.compareIds.set(ids.includes(id)?ids.filter(x=>x!==id):ids.length<4?[...ids,id]:ids);}
 compareItems(){return this.ipos().filter(x=>this.compareIds().includes(x.ipo.id));}
 financialSeries(d:IpoDetail){return [{name:'Revenue',data:d.financials.map(x=>x.revenueCrore)},{name:'PAT',data:d.financials.map(x=>x.patCrore)}]}
 marketSeries(d:IpoDetail){return [{name:'GMP',data:[...d.marketHistory].reverse().map(x=>x.gmp)}]}
 financialLabels(d:IpoDetail){return d.financials.map(x=>x.periodEnd.slice(0,4))}
 marketLabels(d:IpoDetail){return [...d.marketHistory].reverse().map(x=>x.observedAt.slice(5,10))}
}
