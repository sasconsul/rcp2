import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Pages } from './pages.model';
import { PagesPopupService } from './pages-popup.service';
import { PagesService } from './pages.service';

@Component({
    selector: 'jhi-pages-delete-dialog',
    templateUrl: './pages-delete-dialog.component.html'
})
export class PagesDeleteDialogComponent {

    pages: Pages;

    constructor(
        private pagesService: PagesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pagesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'pagesListModification',
                content: 'Deleted an pages'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pages-delete-popup',
    template: ''
})
export class PagesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pagesPopupService: PagesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.pagesPopupService
                .open(PagesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
