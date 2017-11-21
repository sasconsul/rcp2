import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Words } from './words.model';
import { WordsPopupService } from './words-popup.service';
import { WordsService } from './words.service';

@Component({
    selector: 'jhi-words-delete-dialog',
    templateUrl: './words-delete-dialog.component.html'
})
export class WordsDeleteDialogComponent {

    words: Words;

    constructor(
        private wordsService: WordsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.wordsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'wordsListModification',
                content: 'Deleted an words'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-words-delete-popup',
    template: ''
})
export class WordsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wordsPopupService: WordsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.wordsPopupService
                .open(WordsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
