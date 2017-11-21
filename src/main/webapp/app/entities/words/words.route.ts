import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { WordsComponent } from './words.component';
import { WordsDetailComponent } from './words-detail.component';
import { WordsPopupComponent } from './words-dialog.component';
import { WordsDeletePopupComponent } from './words-delete-dialog.component';

export const wordsRoute: Routes = [
    {
        path: 'words',
        component: WordsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Words'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'words/:id',
        component: WordsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Words'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const wordsPopupRoute: Routes = [
    {
        path: 'words-new',
        component: WordsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Words'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'words/:id/edit',
        component: WordsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Words'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'words/:id/delete',
        component: WordsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Words'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
