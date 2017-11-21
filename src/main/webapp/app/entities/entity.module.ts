import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { Rcp2PagesModule } from './pages/pages.module';
import { Rcp2WordsModule } from './words/words.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        Rcp2PagesModule,
        Rcp2WordsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Rcp2EntityModule {}
