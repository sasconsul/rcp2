import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Words } from './words.model';
import { WordsService } from './words.service';

@Component({
    selector: 'jhi-words-detail',
    templateUrl: './words-detail.component.html'
})
export class WordsDetailComponent implements OnInit, OnDestroy {

    words: Words;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private wordsService: WordsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWords();
    }

    load(id) {
        this.wordsService.find(id).subscribe((words) => {
            this.words = words;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWords() {
        this.eventSubscriber = this.eventManager.subscribe(
            'wordsListModification',
            (response) => this.load(this.words.id)
        );
    }
}
