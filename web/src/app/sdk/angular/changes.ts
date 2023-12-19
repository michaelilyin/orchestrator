import {SimpleChange, SimpleChanges} from "@angular/core";

export declare class TypedChange<T> extends SimpleChange {
  previousValue: T | null;
  currentValue: T | null;
  firstChange: boolean;
}

export type TypedChanges<T> = SimpleChanges & { [P in keyof T]?: TypedChange<T[P]> };
