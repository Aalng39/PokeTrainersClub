import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-search-error',
  templateUrl: './search-error.component.html',
  styleUrls: ['./search-error.component.css']
})
export class SearchErrorComponent implements OnInit{

  params$!: Subscription
  name!: string

  constructor(private activatedRoute: ActivatedRoute){ }
                      
  ngOnInit(): void {
    document.body.className = "error"
    this.params$ = this.activatedRoute.params.subscribe(
      (params) => {
        const nameOrId = params['nameOrId']
        this.name = nameOrId
      })
      
  }
    
  ngOnDestroy(){
    document.body.className=""
  }
}
