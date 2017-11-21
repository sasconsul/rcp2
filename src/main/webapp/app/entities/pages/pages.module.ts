import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Rcp2SharedModule } from '../../shared';
import {
    PagesService,
    PagesPopupService,
    PagesComponent,
    PagesDetailComponent,
    PagesDialogComponent,
    PagesPopupComponent,
    PagesDeletePopupComponent,
    PagesDeleteDialogComponent,
    pagesRoute,
    pagesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...pagesRoute,
    ...pagesPopupRoute,
];

@NgModule({
    imports: [
        Rcp2SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PagesComponent,
        PagesDetailComponent,
        PagesDialogComponent,
        PagesDeleteDialogComponent,
        PagesPopupComponent,
        PagesDeletePopupComponent,
    ],
    entryComponents: [
        PagesComponent,
        PagesDialogComponent,
        PagesPopupComponent,
        PagesDeleteDialogComponent,
        PagesDeletePopupComponent,
    ],
    providers: [
        PagesService,
        PagesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Rcp2PagesModule {}
