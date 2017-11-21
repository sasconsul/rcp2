import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Words } from './words.model';
import { WordsService } from './words.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-words',
    templateUrl: './words.component.html'
})
export class WordsComponent implements OnInit, OnDestroy {
words: Words[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private wordsService: WordsService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.wordsService.query().subscribe(
            (res: ResponseWrapper) => {
                this.words = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInWords();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Words) {
        return item.id;
    }
    registerChangeInWords() {
        this.eventSubscriber = this.eventManager.subscribe('wordsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
