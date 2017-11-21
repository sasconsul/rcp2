import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Words } from './words.model';
import { WordsPopupService } from './words-popup.service';
import { WordsService } from './words.service';
import { Pages, PagesService } from '../pages';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-words-dialog',
    templateUrl: './words-dialog.component.html'
})
export class WordsDialogComponent implements OnInit {

    words: Words;
    isSaving: boolean;

    pages: Pages[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private wordsService: WordsService,
        private pagesService: PagesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.pagesService.query()
            .subscribe((res: ResponseWrapper) => { this.pages = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.words.id !== undefined) {
            this.subscribeToSaveResponse(
                this.wordsService.update(this.words));
        } else {
            this.subscribeToSaveResponse(
                this.wordsService.create(this.words));
        }
    }

    private subscribeToSaveResponse(result: Observable<Words>) {
        result.subscribe((res: Words) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Words) {
        this.eventManager.broadcast({ name: 'wordsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPagesById(index: number, item: Pages) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-words-popup',
    template: ''
})
export class WordsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wordsPopupService: WordsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.wordsPopupService
                    .open(WordsDialogComponent as Component, params['id']);
            } else {
                this.wordsPopupService
                    .open(WordsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
