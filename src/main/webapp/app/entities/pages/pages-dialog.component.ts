import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Pages } from './pages.model';
import { PagesPopupService } from './pages-popup.service';
import { PagesService } from './pages.service';

@Component({
    selector: 'jhi-pages-dialog',
    templateUrl: './pages-dialog.component.html'
})
export class PagesDialogComponent implements OnInit {

    pages: Pages;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private pagesService: PagesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.pages.id !== undefined) {
            this.subscribeToSaveResponse(
                this.pagesService.update(this.pages));
        } else {
            this.subscribeToSaveResponse(
                this.pagesService.create(this.pages));
        }
    }

    private subscribeToSaveResponse(result: Observable<Pages>) {
        result.subscribe((res: Pages) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Pages) {
        this.eventManager.broadcast({ name: 'pagesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-pages-popup',
    template: ''
})
export class PagesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pagesPopupService: PagesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.pagesPopupService
                    .open(PagesDialogComponent as Component, params['id']);
            } else {
                this.pagesPopupService
                    .open(PagesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
