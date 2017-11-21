/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { Rcp2TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PagesDetailComponent } from '../../../../../../main/webapp/app/entities/pages/pages-detail.component';
import { PagesService } from '../../../../../../main/webapp/app/entities/pages/pages.service';
import { Pages } from '../../../../../../main/webapp/app/entities/pages/pages.model';

describe('Component Tests', () => {

    describe('Pages Management Detail Component', () => {
        let comp: PagesDetailComponent;
        let fixture: ComponentFixture<PagesDetailComponent>;
        let service: PagesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [Rcp2TestModule],
                declarations: [PagesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PagesService,
                    JhiEventManager
                ]
            }).overrideTemplate(PagesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PagesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PagesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Pages(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.pages).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
