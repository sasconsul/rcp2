import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Rcp2SharedModule } from '../../shared';
import {
    WordsService,
    WordsPopupService,
    WordsComponent,
    WordsDetailComponent,
    WordsDialogComponent,
    WordsPopupComponent,
    WordsDeletePopupComponent,
    WordsDeleteDialogComponent,
    wordsRoute,
    wordsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...wordsRoute,
    ...wordsPopupRoute,
];

@NgModule({
    imports: [
        Rcp2SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        WordsComponent,
        WordsDetailComponent,
        WordsDialogComponent,
        WordsDeleteDialogComponent,
        WordsPopupComponent,
        WordsDeletePopupComponent,
    ],
    entryComponents: [
        WordsComponent,
        WordsDialogComponent,
        WordsPopupComponent,
        WordsDeleteDialogComponent,
        WordsDeletePopupComponent,
    ],
    providers: [
        WordsService,
        WordsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Rcp2WordsModule {}
