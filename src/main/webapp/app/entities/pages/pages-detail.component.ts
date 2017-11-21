import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Pages } from './pages.model';
import { PagesService } from './pages.service';

@Component({
    selector: 'jhi-pages-detail',
    templateUrl: './pages-detail.component.html'
})
export class PagesDetailComponent implements OnInit, OnDestroy {

    pages: Pages;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private pagesService: PagesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPages();
    }

    load(id) {
        this.pagesService.find(id).subscribe((pages) => {
            this.pages = pages;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPages() {
        this.eventSubscriber = this.eventManager.subscribe(
            'pagesListModification',
            (response) => this.load(this.pages.id)
        );
    }
}
