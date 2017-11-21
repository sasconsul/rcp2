import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Pages } from './pages.model';
import { PagesService } from './pages.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-pages',
    templateUrl: './pages.component.html'
})
export class PagesComponent implements OnInit, OnDestroy {
pages: Pages[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private pagesService: PagesService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.pagesService.query().subscribe(
            (res: ResponseWrapper) => {
                this.pages = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInPages();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Pages) {
        return item.id;
    }
    registerChangeInPages() {
        this.eventSubscriber = this.eventManager.subscribe('pagesListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
