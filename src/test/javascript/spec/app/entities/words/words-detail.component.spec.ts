/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { Rcp2TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { WordsDetailComponent } from '../../../../../../main/webapp/app/entities/words/words-detail.component';
import { WordsService } from '../../../../../../main/webapp/app/entities/words/words.service';
import { Words } from '../../../../../../main/webapp/app/entities/words/words.model';

describe('Component Tests', () => {

    describe('Words Management Detail Component', () => {
        let comp: WordsDetailComponent;
        let fixture: ComponentFixture<WordsDetailComponent>;
        let service: WordsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [Rcp2TestModule],
                declarations: [WordsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    WordsService,
                    JhiEventManager
                ]
            }).overrideTemplate(WordsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WordsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WordsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Words(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.words).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
