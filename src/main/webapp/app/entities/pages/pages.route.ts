import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PagesComponent } from './pages.component';
import { PagesDetailComponent } from './pages-detail.component';
import { PagesPopupComponent } from './pages-dialog.component';
import { PagesDeletePopupComponent } from './pages-delete-dialog.component';

export const pagesRoute: Routes = [
    {
        path: 'pages',
        component: PagesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pages'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pages/:id',
        component: PagesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pages'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pagesPopupRoute: Routes = [
    {
        path: 'pages-new',
        component: PagesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pages/:id/edit',
        component: PagesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pages/:id/delete',
        component: PagesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
