import { DatePipe, DecimalPipe, KeyValuePipe, NgClass } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IpoSummary } from './models';

@Component({selector:'app-root',standalone:true,imports:[DatePipe,DecimalPipe,KeyValuePipe,NgClass,FormsModule],templateUrl:'./app.component.html'})
export class AppComponent {
 private http=inject(HttpClient); ipos=signal<IpoSummary[]>([]); loading=signal(true); error=signal(''); query=signal(''); status=signal('ALL'); selected=signal<IpoSummary|null>(null);
 filtered=computed(()=>this.ipos().filter(x=>(this.status()==='ALL'||x.ipo.status===this.status())&&x.ipo.companyName.toLowerCase().includes(this.query().toLowerCase())));
 openCount=computed(()=>this.ipos().filter(x=>x.ipo.status==='OPEN').length);
 averageScore=computed(()=>this.ipos().length?Math.round(this.ipos().reduce((a,b)=>a+b.analysis.overallScore,0)/this.ipos().length):0);
 constructor(){this.http.get<IpoSummary[]>('/api/v1/ipos').subscribe({next:v=>{this.ipos.set(v);this.loading.set(false)},error:()=>{this.error.set('The API is unavailable. Start the Spring Boot service and refresh.');this.loading.set(false)}})}
 badge(v:string){return v==='APPLY'?'positive':v==='APPLY_WITH_CAUTION'?'caution':v==='AVOID'?'negative':'muted'}
}
